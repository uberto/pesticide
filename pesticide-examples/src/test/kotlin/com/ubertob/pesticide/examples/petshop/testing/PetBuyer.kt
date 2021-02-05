package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.core.DdtActorWithContext
import com.ubertob.pesticide.examples.petshop.model.Pet
import org.junit.jupiter.api.Assertions.fail
import strikt.api.expectThat
import strikt.assertions.*

typealias CartId = Int

data class PetBuyer(override val name: String) : DdtActorWithContext<PetShopInterpreter, CartId>() {

    fun `check that the price of $ is $`(petName: String, expectedPrice: Int) =
        step(petName, expectedPrice) {
            val price = askPetPrice(petName)
            expectThat(price).isEqualTo(expectedPrice)
        }

    fun `put $ into the cart`(petName: String) =
        step(petName) { cxt ->
            expectThat(cxt.getOrNull()).isNull()
            val cartId = createNewCart() ?: fail("No CartId")
            addToCart(cartId, petName)
            cxt.store(cartId)
        }


    fun `cannot put $ into the cart`(petName: String) =
        step(petName) { cxt ->
            expectThat(cxt.getOrNull()).isNotNull()

            //addToCart(cartId, petName)
        }

    fun `checkout with pets $`(vararg pets: String) =
        step(pets.asList().joinToString(",")) { ctx ->
            val cartId = ctx.get()
            val cart = askCartStatus(cartId)
            val petList = cart?.pets?.map(Pet::name).orEmpty()
            expectThat(petList).containsExactly(pets.toList())
            checkOut(cartId)
        }

    fun `check that there are no more $ for sale`(petName: String) =
        step(petName) {
            val pets = askPetList()
            expectThat(pets.orEmpty()).doesNotContain(petName)
        }

}