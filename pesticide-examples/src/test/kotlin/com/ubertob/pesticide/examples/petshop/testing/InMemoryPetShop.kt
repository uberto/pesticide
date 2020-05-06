package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.DomainSetUp
import com.ubertob.pesticide.InMemoryHubs
import com.ubertob.pesticide.Ready
import com.ubertob.pesticide.examples.petshop.model.Pet
import com.ubertob.pesticide.examples.petshop.model.PetShopHub

class InMemoryPetShop() : PetShopInterpreter {

    private val hub = PetShopHub()

    override val protocol = InMemoryHubs

    override fun prepare(): DomainSetUp = Ready

    override fun populateShop(vararg pets: Pet): PetShopInterpreter =
        apply {
            pets.forEach {
                hub.addPet(it)
            }
        }


    override fun BuyPet.tryIt(): InMemoryPetShop {
        hub.buyPet(petName)
        return this@InMemoryPetShop
    }


    override fun PetPrice.askIt() =
        this@InMemoryPetShop.apply {
            verifyBlock(hub.getByName(petName)?.price)
        }

    override fun PetList.askIt(): PetShopInterpreter =
        this@InMemoryPetShop.apply {
            verifyBlock(hub.getAll())
        }


}