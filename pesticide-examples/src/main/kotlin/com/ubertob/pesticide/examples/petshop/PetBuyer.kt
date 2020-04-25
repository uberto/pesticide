package com.ubertob.pesticide.examples.petshop

import com.ubertob.pesticide.DdtActor
import com.ubertob.pesticide.DdtStep

data class PetBuyer(override val name: String) : DdtActor<PetShopDomain>() {
    fun `check the price of`(petName: String, expectedPrice: Int): DdtStep<PetShopDomain> {
        return TODO("not implemented")
    }

    fun `buy a`(petName: String): DdtStep<PetShopDomain> {
        return TODO("not implemented")
    }

}