package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.DomainSetUp
import com.ubertob.pesticide.NotReady
import com.ubertob.pesticide.PureHttp
import com.ubertob.pesticide.Ready
import com.ubertob.pesticide.examples.petshop.http.PetShopHandler
import com.ubertob.pesticide.examples.petshop.model.Pet
import com.ubertob.pesticide.examples.petshop.model.PetShopHub
import org.http4k.server.Jetty
import org.http4k.server.asServer

class HttpRestPetshopDomain(val host: String, val port: Int) : PetShopDomainWrapper {
    override fun populateShop(vararg pets: Pet): PetShopDomainWrapper = apply {

    }

    override fun BuyPet.tryIt(): PetShopDomainWrapper = TODO("not implemented")

    override fun PetPrice.askIt(): PetShopDomainWrapper = TODO("not implemented")

    override val protocol = PureHttp("local")

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


}