package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.core.DdtProtocol
import com.ubertob.pesticide.core.DomainInterpreter
import com.ubertob.pesticide.examples.petshop.model.Cart
import com.ubertob.pesticide.examples.petshop.model.Pet

interface PetShopInterpreter : DomainInterpreter<DdtProtocol> {

    fun populateShop(vararg pets: Pet)

    fun askPetPrice(petName: String): Int?
    fun askPetList(): List<String>?
    fun askCartStatus(cartId: CartId): Cart?

    fun createNewCart(): CartId?
    fun addToCart(cartId: CartId, petName: String)
    fun checkOut(cartId: CartId)
}

val allPetShopInterpreters = setOf(
    DomainOnlyPetShop(),
    HttpRestPetshop("localhost", 8082)
)


