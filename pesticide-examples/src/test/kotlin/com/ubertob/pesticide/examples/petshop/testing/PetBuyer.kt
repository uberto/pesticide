package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.core.DdtActorWithContext
import com.ubertob.pesticide.examples.petshop.model.Pet
import org.junit.jupiter.api.Assertions.fail
import strikt.api.expectThat
import strikt.assertions.containsExactly
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

    fun `put $ into the cart`(petName: String) =
        step(petName) { cxt ->
            val cartId = cxt.context ?: NewCart.createIt() ?: fail("No CartId")
            AddToCart(cartId, petName).tryIt()
            cxt.updateContext(cartId)
        }

    fun `checkout with pets $`(vararg pets: String) =
        step(pets.asList().joinToString(",")) { ctx ->
            val cartId = ctx.context!!
            CartStatus(cartId) { cart ->
                val petList = cart?.pets?.map(Pet::name).orEmpty()
                expectThat(petList).containsExactly(pets.toList())
            }.askIt()


            CheckOut(cartId).tryIt()
        }

    fun `check that there are no more $ for sale`(petName: String) =
        step(petName) {
            PetList() { pets ->
                expectThat(pets.orEmpty()).doesNotContain(petName)
            }.askIt()
        }

}