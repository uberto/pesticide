package com.ubertob.pesticide.examples.petshop.http

import com.ubertob.kondor.outcome.recover
import com.ubertob.pesticide.examples.petshop.model.PetShopHub
import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes


class PetShopHandler(val hub: PetShopHub) : HttpHandler {


    override fun invoke(request: Request) = petShopRoutes(request)

    val petShopRoutes: HttpHandler = routes(
        "/pets" bind Method.GET to ::listPets,
        "/pets/{name}" bind Method.GET to ::petDetails,
        "/pets" bind Method.POST to ::addPetToShop,
        "/cart" bind Method.POST to ::createCart,
        "/cart/{cartId}" bind Method.GET to ::cartDetails,
        "/cart/{cartId}/add/{petName}" bind Method.PUT to ::addPetToCart,
        "/cart/{cartId}/checkout" bind Method.PUT to ::checkout
    )

    fun petDetails(request: Request): Response =
        request.path("name")
            ?.let(hub::getByName)
            ?.let(JPet::toJson)
            ?.let(Response(Status.OK)::body)
            ?: Response(Status.NOT_FOUND)


    fun addPetToCart(request: Request): Response =
        request.path("cartId")
            ?.let { cartId ->
                request.path("petName")?.let {
                    hub.addPetToCart(cartId.asCartId(), it)
                    Response(Status.ACCEPTED)
                }
            } ?: Response(Status.BAD_REQUEST)

    fun addPetToShop(request: Request): Response =
        JPet.fromJson(request.bodyString()).transform {
            hub.addPet(it)
            Response(Status.ACCEPTED)
        }.recover {
            Response(Status.BAD_REQUEST)
        }

    fun createCart(request: Request): Response =
        Response(Status.ACCEPTED).body(
            JCart.toJson(hub.createCart())
        )

    fun listPets(request: Request): Response =
        Response(Status.OK).body(
            jPetNames.toJson(hub.getAll())
        )

    fun cartDetails(request: Request): Response =
        request.path("cartId")
            ?.let { hub.getCart(it.asCartId()) }
            ?.let {
                Response(Status.OK)
                    .body(JCart.toJson(it))
            } ?: Response(Status.BAD_REQUEST)

    fun checkout(request: Request): Response =
        request.path("cartId")
            ?.let {
                hub.cartCheckout(it.asCartId())?.let { cart ->
                    Response(Status.ACCEPTED).body(
                        JCart.toJson(cart)
                    )
                }
            } ?: Response(Status.NOT_FOUND)


    fun String.asCartId(): Int = toIntOrNull() ?: -1
}
