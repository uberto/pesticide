package com.ubertob.pesticide.examples.petshop.http

import com.beust.klaxon.Klaxon
import com.ubertob.pesticide.examples.petshop.model.Pet
import com.ubertob.pesticide.examples.petshop.model.PetShopHub
import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes


class PetShopHandler(val hub: PetShopHub) : HttpHandler {

    val petShopRoutes: HttpHandler = routes(
        "/pets" bind Method.GET to ::listPets,
        "/pets/{name}" bind Method.GET to ::petDetails,
        "/pets" bind Method.POST to ::addPet,
        "/pets/{name}/buy" bind Method.PUT to ::buyPet
    )

    fun petDetails(request: Request): Response =
        request.path("name")
            ?.let { hub.getByName(it) }
            ?.let {
                Response(Status.OK)
                    .body(klaxon.toJsonString(it))
            } ?: Response(Status.BAD_REQUEST)

    fun buyPet(request: Request): Response =
        request.path("name")
            ?.let {
                hub.buyPet(it)
                Response(Status.ACCEPTED)
            } ?: Response(Status.BAD_REQUEST)

    fun addPet(request: Request): Response =
        klaxon.parse<Pet>(request.bodyString())
            ?.let {
                hub.addPet(it)
                Response(Status.ACCEPTED)
            } ?: Response(Status.BAD_REQUEST)


    fun listPets(request: Request): Response =
        Response(Status.OK).body(
            klaxon.toJsonString(hub.getAll())
        )

    val klaxon = Klaxon()

    override fun invoke(request: Request): Response = petShopRoutes(request)
}
