package com.ubertob.pesticide.examples.petshop.http

import com.ubertob.pesticide.examples.petshop.model.PetShopHub
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.bind
import org.http4k.routing.routes


class PetShopHandler(val hub: PetShopHub) : HttpHandler {

    val petShopRoutes: HttpHandler = routes(
        "/pets" bind Method.GET to ::listPets,
        "/pets/{name}" bind Method.GET to ::petDetails,
        "/pets" bind Method.POST to ::addPet,
        "/pets/{name}/buy" bind Method.PUT to ::buyPet
    )

    fun petDetails(request: Request): Response {
        return TODO("not implemented")
    }

    fun buyPet(request: Request): Response {
        return TODO("not implemented")
    }

    fun addPet(request: Request): Response {
        return TODO("not implemented")
    }

    fun listPets(request: Request): Response {
        return TODO("not implemented")
    }

    override fun invoke(request: Request): Response = petShopRoutes(request)
}
