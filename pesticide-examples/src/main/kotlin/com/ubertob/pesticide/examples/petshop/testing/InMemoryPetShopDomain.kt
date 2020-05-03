package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.DomainSetUp
import com.ubertob.pesticide.InMemoryHubs
import com.ubertob.pesticide.Ready
import com.ubertob.pesticide.examples.petshop.model.Pet
import com.ubertob.pesticide.examples.petshop.model.PetShopHub

class InMemoryPetShopDomain() : PetShopDomainWrapper {

    private val hub = PetShopHub()

    override val protocol = InMemoryHubs

    override fun prepare(): DomainSetUp = Ready

    override fun populateShop(vararg pets: Pet): PetShopDomainWrapper =
        apply {
            pets.forEach {
                hub.addPet(it)
            }
        }


    override fun BuyPet.tryIt(): InMemoryPetShopDomain {
        hub.buyPet(petName)
        return this@InMemoryPetShopDomain
    }


    override fun PetPrice.askIt() =
        this@InMemoryPetShopDomain.also {
            verifyBlock(hub.getByName(petName)?.price)
        }


}