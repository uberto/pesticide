package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.DdtActor
import com.ubertob.pesticide.DdtStep
import com.ubertob.pesticide.examples.petshop.model.Pet
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.doesNotContain
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull


data class ShopAssistant(override val name: String) : DdtActor<PetShopInterpreter>() {

    fun `check that $ is in the shop`(pet: Pet) = step(pet.name) {
        PetList { pets: List<String>? ->
            expectThat(pets).isNotNull().contains(pet.name)
        }.askIt()
    }
}

data class PetBuyer(override val name: String) : DdtActor<PetShopInterpreter>() {

    fun `check that the price of $ is $`(petName: String, expectedPrice: Int) =
        step(petName, expectedPrice) {
            PetPrice(petName) { price ->
                expectThat(price).isEqualTo(expectedPrice)
            }.askIt()
        }

    fun `buy a $`(petName: String) =
        step(petName) {
            BuyPet(petName).tryIt()
        }

    fun `check that there are no more $ for sale`(petName: String): DdtStep<PetShopInterpreter> =
        step(petName) {
            PetList() { pets ->
                expectThat(pets.orEmpty()).doesNotContain(petName)
            }.askIt()
        }

}