package com.ubertob.pesticide.examples.petshop

import com.ubertob.pesticide.DdtActor
import com.ubertob.pesticide.DdtStep
import com.ubertob.pesticide.examples.petshop.PetShopAction.AskPrice
import com.ubertob.pesticide.examples.petshop.PetShopAction.BuyPet
import strikt.api.expectThat
import strikt.assertions.isEqualTo

data class PetBuyer(override val name: String) : DdtActor<PetShopDomainWrapper>() {
    fun `check that the price of $ is $`(petName: String, expectedPrice: Int): DdtStep<PetShopDomainWrapper> =
        step(petName, expectedPrice) {
            AskPrice(petName) { price ->
                expectThat(expectedPrice).isEqualTo(price)
            }.doIt()

        }


    fun `buy a $`(petName: String): DdtStep<PetShopDomainWrapper> =
        step(petName) {
            BuyPet(petName).doIt()
        }

}