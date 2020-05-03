package com.ubertob.pesticide.examples.petshop

import com.ubertob.pesticide.DDT
import com.ubertob.pesticide.DomainDrivenTest
import com.ubertob.pesticide.NamedActor
import com.ubertob.pesticide.examples.petshop.model.Pet
import com.ubertob.pesticide.examples.petshop.testing.PetBuyer
import com.ubertob.pesticide.examples.petshop.testing.PetShopDomainWrapper
import com.ubertob.pesticide.examples.petshop.testing.allPetShopAbstractions


class PetShopDDT : DomainDrivenTest<PetShopDomainWrapper>(
    allPetShopAbstractions()
) {

    val mary by NamedActor(::PetBuyer)

    @DDT
    fun `mary buys a lamb`() = ddtScenario {
        val lamb = Pet("lamb", 64)
        val hamster = Pet("hamster", 128)
        setting {
            populateShop(lamb, hamster)
        } atRise play(
            mary.`check that the price of $ is $`("lamb", 64),
            mary.`check that the price of $ is $`("hamster", 128),
            mary.`buy a $`("lamb"),
            mary.`check that there are no more $ for sale`("lamb")
        )
    }


}


