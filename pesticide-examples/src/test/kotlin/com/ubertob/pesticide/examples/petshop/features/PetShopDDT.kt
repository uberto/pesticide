package com.ubertob.pesticide.examples.petshop.features

import com.ubertob.pesticide.core.DDT
import com.ubertob.pesticide.core.DomainDrivenTest
import com.ubertob.pesticide.examples.petshop.model.Pet
import com.ubertob.pesticide.examples.petshop.testing.PetBuyer
import com.ubertob.pesticide.examples.petshop.testing.PetShopCrossActions
import com.ubertob.pesticide.examples.petshop.testing.ShopAssistant
import com.ubertob.pesticide.examples.petshop.testing.allPetShopActionss


class PetShopDDT : DomainDrivenTest<PetShopCrossActions>(allPetShopActionss) {

    val mary by NamedActor(::PetBuyer)
    val bert by NamedActor(::PetBuyer)

    val adam by NamedActor(::ShopAssistant)


    @DDT
    fun `mary buys a lamb`() = ddtScenario {
        val lamb = Pet("lamb", 64)
        val hamster = Pet("hamster", 12)
        setUp {
            populateShop(lamb, hamster)
        }.thenPlay(
            mary.`check that the price of #pet is #price`(pet = "lamb", price = 64),
            mary.`check that the price of #pet is #price`(pet = "hamster", price = 12),
            mary.`put # into the cart`("lamb"),
            mary.`check that there are no more # for sale`("lamb"),
            mary.`checkout with pets #`("lamb")
        )
    }

    @DDT
    fun `mary buys a lamb and bert buys a hamster`() = ddtScenario {
        val lamb = Pet("lamb", 64)
        val hamster = Pet("hamster", 12)
        setUp {
            populateShop(lamb, hamster)
        }.thenPlay(
            mary.`put # into the cart`("lamb"),
            bert.`check that there are no more # for sale`("lamb"),
            bert.`put # into the cart`("hamster"),
            mary.`checkout with pets #`("lamb"),
            bert.`checkout with pets #`("hamster")
        )
    }

    @DDT
    fun `shop assistance check the pets`() = ddtScenario {
        val parrot = Pet("parrot", 100)
        val bunny = Pet("bunny", 70)
        setUp {
            populateShop(parrot, bunny)
        }.thenPlay(
            adam.`check that # is in the shop`(parrot),
            adam.`check that # is in the shop`(bunny)
        )
    }

    //check that cannot add to cart after checkout
    @DDT
    fun `mary cannot put another pet in the cart after she checked out`() = ddtScenario {
        val lamb = Pet("lamb", 64)
        val hamster = Pet("hamster", 12)
        setUp {
            populateShop(lamb, hamster)
        }.thenPlay(
            mary.`put # into the cart`("lamb"),
            mary.`checkout with pets #`("lamb"),
            mary.`cannot put # into the cart`("hamster")
        )
    }

    //exercise for workshop
    @DDT
    fun `Bert cannot buy same pet twice in the cart`() = ddtScenario {
        val parrot = Pet("parrot", 100)
        val bunny = Pet("bunny", 70)
        setUp {
            populateShop(parrot, bunny)
        }.thenPlay(
            mary.`put # into the cart`(parrot.name),
            mary.`checkout with pets #`(parrot.name),
            bert.`cannot put # into the cart`(parrot.name)
        )
    }


}


