package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.DdtActorWithContext
import strikt.api.expectThat
import strikt.assertions.doesNotContain
import strikt.assertions.isEqualTo

typealias CartId = Int

data class PetBuyer(override val name: String) : DdtActorWithContext<PetShopInterpreter, CartId>() {

    fun `check that the price of $ is $`(petName: String, expectedPrice: Int) =
        step(petName, expectedPrice) {
            PetPrice(petName) { price ->
                expectThat(price).isEqualTo(expectedPrice)
            }.askIt()
        }

//    fun `put $ into the cart`(petName: String) =
//        stepAndUpdate(petName) {
//            val cartId = GetCart(){ }.askIt()
//
//            PutPetIntoCart(petName, cartId).tryIt()
//            cartId
//        }
//
//    fun `checkout with pets $`(pets: List<String>) =
//        step(pets) {
//            BuyPet(petName).tryIt()
//        }

    fun `check that there are no more $ for sale`(petName: String) =
        step(petName) {
            PetList() { pets ->
                expectThat(pets.orEmpty()).doesNotContain(petName)
            }.askIt()
        }

}