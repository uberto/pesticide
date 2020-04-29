package com.ubertob.pesticide.examples.petshop

import com.ubertob.pesticide.InMemoryHubs

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

    override fun isReady() = true


}