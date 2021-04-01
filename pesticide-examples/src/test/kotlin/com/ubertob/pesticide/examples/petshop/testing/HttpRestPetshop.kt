package com.ubertob.pesticide.examples.petshop.testing

import com.beust.klaxon.Klaxon
import com.ubertob.pesticide.core.DomainSetUp
import com.ubertob.pesticide.core.Http
import com.ubertob.pesticide.core.NotReady
import com.ubertob.pesticide.core.Ready
import com.ubertob.pesticide.examples.petshop.http.PetShopHandler
import com.ubertob.pesticide.examples.petshop.model.Cart
import com.ubertob.pesticide.examples.petshop.model.Pet
import com.ubertob.pesticide.examples.petshop.model.PetShopHub
import org.http4k.client.JettyClient
import org.http4k.core.Method
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.server.Jetty
import org.http4k.server.asServer
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class HttpRestPetshop(val host: String, val port: Int) : PetShopInterpreter, PetShopCrossInterpreter {

    override val protocol = Http("$host:$port")

    var started = false

    override fun prepare(): DomainSetUp = try {
        if (host == "localhost" && !started) {
            started = true
            println("Pets example started listening on port $port")
            val server = PetShopHandler(PetShopHub()).asServer(Jetty(port)).start()
            registerShutdownHook {
                server.stop()
            }
        }
        Ready
    } catch (t: Throwable) {
        NotReady(t.toString())
    }

    private fun registerShutdownHook(hookToExecute: () -> Unit) {
        Runtime.getRuntime().addShutdownHook(Thread {
            val out = System.out
            try {
                hookToExecute()
            } finally {
                System.setOut(out)
            }
        })
    }

    val client = JettyClient()

    private fun uri(path: String) = "http://$host:$port/$path"

    fun addPetRequest(pet: Pet) = Request(POST, uri("pets")).body(pet.toJson())

    override fun populateShop(vararg pets: Pet) =
        pets.forEach {
            val resp = client(addPetRequest(it))
            expectThat(resp.status).isEqualTo(ACCEPTED)
        }

    override fun askPetPrice(petName: String): Int? {
        val req = Request(GET, uri("pets/${petName}"))
        val resp = client(req)

        if (resp.status == NOT_FOUND)
            return null

        expectThat(resp.status).isEqualTo(OK)

        val pet = klaxon.parse<Pet>(resp.bodyString())

        return pet?.price

    }

    override fun askPetList(): List<String>? {
        val req = Request(GET, uri("pets"))
        val resp = client(req)

        expectThat(resp.status).isEqualTo(OK)

        val pets = klaxon.parseArray<String>(resp.bodyString())

        return (pets)
    }

    override fun askCartStatus(cartId: CartId): Cart? {
        val req = Request(GET, uri("cart/$cartId"))
        val resp = client(req)

        expectThat(resp.status).isEqualTo(OK)

        return klaxon.parse<Cart>(resp.bodyString())
    }

    override fun createNewCart(): CartId? {
        val req = Request(POST, uri("cart"))
        val resp = client(req)
        expectThat(resp.status).isEqualTo(ACCEPTED)
        return klaxon.parse<Cart>(resp.bodyString())?.id
    }

    override fun addToCart(cartId: CartId, petName: String) {
        val req = Request(Method.PUT, uri("cart/$cartId/add/$petName"))
        val resp = client(req)
        expectThat(resp.status).isEqualTo(ACCEPTED)
    }

    override fun checkOut(cartId: CartId) {
        val req = Request(Method.PUT, uri("cart/$cartId/checkout"))
        val resp = client(req)
        expectThat(resp.status).isEqualTo(ACCEPTED)
    }

    val klaxon = Klaxon()

    private fun Pet.toJson(): String = klaxon.toJsonString(this)

}