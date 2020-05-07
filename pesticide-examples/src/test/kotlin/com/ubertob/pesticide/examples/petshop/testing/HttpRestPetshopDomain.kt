package com.ubertob.pesticide.examples.petshop.testing

import com.beust.klaxon.Klaxon
import com.ubertob.pesticide.DomainSetUp
import com.ubertob.pesticide.NotReady
import com.ubertob.pesticide.PureHttp
import com.ubertob.pesticide.Ready
import com.ubertob.pesticide.examples.petshop.http.PetShopHandler
import com.ubertob.pesticide.examples.petshop.model.Pet
import com.ubertob.pesticide.examples.petshop.model.PetShopHub
import org.http4k.client.JettyClient
import org.http4k.core.Method.*
import org.http4k.core.Request
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.OK
import org.http4k.server.Jetty
import org.http4k.server.asServer
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class HttpRestPetshopDomain(val host: String, val port: Int) : PetShopInterpreter {

    val client = JettyClient()

    private fun uri(path: String) = "http://$host:$port/$path"

    fun addPetRequest(pet: Pet) = Request(POST, uri("pets")).body(pet.toJson())

    override fun populateShop(vararg pets: Pet): PetShopInterpreter = apply {
        pets.forEach {
            val resp = client(addPetRequest(it))
            expectThat(resp.status).isEqualTo(ACCEPTED)
        }
    }

    override fun BuyPet.tryIt() {

        val req = Request(PUT, uri("pets/${petName}/buy"))
        val resp = client(req)
        expectThat(resp.status).isEqualTo(ACCEPTED)

    }

    override fun PetPrice.askIt() {

        val req = Request(GET, uri("pets/${petName}"))
        val resp = client(req)

        expectThat(resp.status).isEqualTo(OK)

        val pet = klaxon.parse<Pet>(resp.bodyString())

        verifyBlock(pet?.price)

    }

    override fun PetList.askIt() {
        val req = Request(GET, uri("pets"))
        val resp = client(req)

        expectThat(resp.status).isEqualTo(OK)

        val pets = klaxon.parseArray<String>(resp.bodyString())

        verifyBlock(pets)
    }

    override val protocol = PureHttp("$host:$port")

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

    val klaxon = Klaxon()

    private fun Pet.toJson(): String = klaxon.toJsonString(this)

}