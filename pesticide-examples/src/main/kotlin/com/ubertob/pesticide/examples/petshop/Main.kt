package com.ubertob.pesticide.examples.petshop

import com.ubertob.pesticide.examples.petshop.http.PetShopHandler
import com.ubertob.pesticide.examples.petshop.model.PetShopHub
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
    val port = 8081
    println("Pets example started listening on port $port")
    val hub = PetShopHub()
    PetShopHandler(hub).asServer(Jetty(port)).start()
}
