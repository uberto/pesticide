package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.kondor.outcome.Failure
import com.ubertob.kondor.outcome.Outcome
import com.ubertob.kondor.outcome.Success
import com.ubertob.pesticide.core.DomainSetUp
import com.ubertob.pesticide.core.Http
import com.ubertob.pesticide.core.NotReady
import com.ubertob.pesticide.core.Ready
import com.ubertob.pesticide.examples.petshop.http.JCart
import com.ubertob.pesticide.examples.petshop.http.JPet
import com.ubertob.pesticide.examples.petshop.http.PetShopHandler
import com.ubertob.pesticide.examples.petshop.http.jPetNames
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

class HttpRestPetshop(val host: String, val port: Int) : PetShopActions, PetShopCrossActions {

    override val protocol = Http("$host:$port")

    private val server = PetShopHandler(PetShopHub()).asServer(Jetty(port))

    override fun prepare(): DomainSetUp = try {
        if (host == "localhost") {
            println("Pets example started listening on port $port")
            server.start()
            registerShutdownHook {
                server.stop()
            }
        }
        Ready
    } catch (t: Throwable) {
        NotReady(t.toString())
    }

    override fun tearDown(): HttpRestPetshop {
        server.stop()
        println("Pets example server stopped")
        return this
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

    fun addPetRequest(pet: Pet) = Request(POST, uri("pets")).body(JPet.toJson(pet))

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

        val pet = JPet.fromJson(resp.bodyString()).orNull()

        return pet?.price

    }

    override fun askPetList(): List<String>? {
        val req = Request(GET, uri("pets"))
        val resp = client(req)

        expectThat(resp.status).isEqualTo(OK)

        val pets = jPetNames.fromJson(resp.bodyString()).orThrow()

        return (pets)
    }

    override fun askCartStatus(cartId: CartId): Cart? {
        val req = Request(GET, uri("cart/$cartId"))
        val resp = client(req)

        expectThat(resp.status).isEqualTo(OK)

        return JCart.fromJson(resp.bodyString()).orThrow()
    }

    override fun createNewCart(): CartId? {
        val req = Request(POST, uri("cart"))
        val resp = client(req)
        expectThat(resp.status).isEqualTo(ACCEPTED)
        return JCart.fromJson(resp.bodyString()).orThrow().id
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

}

private fun <T> Outcome<*, T>.orNull(): T? =
    when (this) {
        is Success -> value
        is Failure -> null
    }
