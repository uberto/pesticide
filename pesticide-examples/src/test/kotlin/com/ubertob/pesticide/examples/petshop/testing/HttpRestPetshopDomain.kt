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

class HttpRestPetshopDomain(val host: String, val port: Int) : PetShopDomainWrapper {

    val client = JettyClient()

    private fun uri(path: String) = "http://$host:$port/$path"

    fun addPetRequest(pet: Pet) = Request(POST, uri("pets")).body(pet.toJson())

    override fun populateShop(vararg pets: Pet): PetShopDomainWrapper = apply {
        pets.forEach {
            val resp = client(addPetRequest(it))
            expectThat(resp.status).isEqualTo(ACCEPTED)
        }
    }

    override fun BuyPet.tryIt(): PetShopDomainWrapper {

        val req = Request(PUT, uri("pets/${petName}/buy"))
        val resp = client(req)
        expectThat(resp.status).isEqualTo(ACCEPTED)

        return this@HttpRestPetshopDomain
    }

    override fun PetPrice.askIt(): PetShopDomainWrapper {

        val req = Request(GET, uri("pets/${petName}"))
        val resp = client(req)

        expectThat(resp.status).isEqualTo(OK)

        val pet = klaxon.parse<Pet>(resp.bodyString())

        verifyBlock(pet?.price)

        return this@HttpRestPetshopDomain

    }

    override fun PetList.askIt(): PetShopDomainWrapper {
        val req = Request(GET, uri("pets"))
        val resp = client(req)

        expectThat(resp.status).isEqualTo(OK)

        val pets = klaxon.parseArray<String>(resp.bodyString())

        verifyBlock(pets)

        return this@HttpRestPetshopDomain
    }

    override val protocol = PureHttp("$host:$port")

    override fun prepare(): DomainSetUp = try {
        if (host == "localhost") {
            println("Pets example started listening on port $port")
            val hub = PetShopHub()
            PetShopHandler(hub).asServer(Jetty(port)).start()
        }
        Ready
    } catch (t: Throwable) {
        NotReady(t.toString())
    }

    val klaxon = Klaxon()

    private fun Pet.toJson(): String = klaxon.toJsonString(this)

}