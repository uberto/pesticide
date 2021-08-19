package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.core.DdtProtocol
import com.ubertob.pesticide.core.DomainActions
import com.ubertob.pesticide.examples.petshop.model.Cart
import com.ubertob.pesticide.examples.petshop.model.Pet

interface PetShopCrossActions : PetShopActions, PetShopSAActions

//to show
interface PetShopSAActions : DomainActions<DdtProtocol> {
    fun askPetList(): List<String>?
}

interface PetShopActions : DomainActions<DdtProtocol> {

    fun populateShop(vararg pets: Pet)
    fun askPetPrice(petName: String): Int?

    fun askCartStatus(cartId: CartId): Cart?

    fun createNewCart(): CartId?
    fun addToCart(cartId: CartId, petName: String)
    fun checkOut(cartId: CartId)
}

val allPetShopActionss = setOf(
    DomainOnlyPetShop(),
    HttpRestPetshop("localhost", 8082)
)


