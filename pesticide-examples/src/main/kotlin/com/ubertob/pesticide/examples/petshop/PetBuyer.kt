package com.ubertob.pesticide.examples.petshop

import com.ubertob.pesticide.DdtActor
import com.ubertob.pesticide.DdtStep
import com.ubertob.pesticide.examples.petshop.PetShopAction.AskPrice
import com.ubertob.pesticide.examples.petshop.PetShopAction.BuyPet
import strikt.api.expectThat
import strikt.assertions.isEqualTo

data class PetBuyer(override val name: String) : DdtActor<PetShopDomain>() {
    fun `check the price of`(petName: String, expectedPrice: Int): DdtStep<PetShopDomain> =
        generateStep("check that the price of $petName is $expectedPrice") {
            AskPrice(petName) { price ->
                expectThat(expectedPrice).isEqualTo(price)
            }.doIt()

        }


    fun `buy a`(petName: String): DdtStep<PetShopDomain> =
        generateStep("buy a $petName") {
            BuyPet(petName).doIt()
        }

}