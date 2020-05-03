package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.DdtActor
import com.ubertob.pesticide.DdtStep
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

data class PetBuyer(override val name: String) : DdtActor<PetShopDomainWrapper>() {

    fun `check that the price of $ is $`(petName: String, expectedPrice: Int): DdtStep<PetShopDomainWrapper> =
        step(petName, expectedPrice) {
            PetPrice(petName) { price ->
                expectThat(price).isEqualTo(expectedPrice)
            }.askIt()
        }

    fun `buy a $`(petName: String): DdtStep<PetShopDomainWrapper> =
        step(petName) {
            BuyPet(petName).tryIt()
        }

    fun `check that there are no more $ for sale`(petName: String): DdtStep<PetShopDomainWrapper> =
        step(petName) {
            PetPrice(petName) { price ->
                expectThat(price).isNull()
            }.askIt()
        }

}