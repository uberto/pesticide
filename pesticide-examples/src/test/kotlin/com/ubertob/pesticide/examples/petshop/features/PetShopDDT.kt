package com.ubertob.pesticide.examples.petshop.features

import com.ubertob.pesticide.DDT
import com.ubertob.pesticide.DomainDrivenTest
import com.ubertob.pesticide.NamedActor
import com.ubertob.pesticide.examples.petshop.model.Pet
import com.ubertob.pesticide.examples.petshop.testing.PetBuyer
import com.ubertob.pesticide.examples.petshop.testing.PetShopInterpreter
import com.ubertob.pesticide.examples.petshop.testing.ShopAssistant
import com.ubertob.pesticide.examples.petshop.testing.allPetShopAbstractions


class PetShopDDT : DomainDrivenTest<PetShopInterpreter>(allPetShopAbstractions) {

    val mary by NamedActor(::PetBuyer)

    val adam by NamedActor(::ShopAssistant)

    @DDT
    fun `shop assistance check the pets`() = ddtScenario {
        val parrot = Pet("parrot", 100)
        val bunny = Pet("bunny", 70)
        setting {
            populateShop(parrot, bunny)
        } atRise play(
            adam.`check that $ is in the shop`(parrot),
            adam.`check that $ is in the shop`(bunny)
        )
    }

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


