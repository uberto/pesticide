package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.DomainSetUp
import com.ubertob.pesticide.InMemoryHubs
import com.ubertob.pesticide.Ready
import com.ubertob.pesticide.examples.petshop.model.Pet

data class InMemoryPetShopDomain(val pets: List<Pet> = emptyList()) :
    PetShopDomainWrapper {
    override fun populateShop(vararg pets: Pet): PetShopDomainWrapper =
        InMemoryPetShopDomain(pets.asList())


    override fun PetShopAction.BuyPet.doIt() =
        this@InMemoryPetShopDomain.copy(pets.filterNot { it.name == petName })

    override fun PetShopAction.AskPrice.doIt() =
        this@InMemoryPetShopDomain.also {
            verifyBlock(pets.first { it.name == petName }.price)
        }

    override val protocol = InMemoryHubs

    override fun prepare(): DomainSetUp = Ready


}