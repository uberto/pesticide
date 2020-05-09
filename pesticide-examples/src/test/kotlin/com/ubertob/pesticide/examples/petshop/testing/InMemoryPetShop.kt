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


    override fun PetPrice.askIt() {
        verifyBlock(hub.getByName(petName)?.price)
    }

    override fun PetList.askIt() {
        verifyBlock(hub.getAll())
    }

    override fun CartStatus.askIt() {
        return TODO("not implemented")
    }

    override fun NewCart.createIt(): CartId {
        return TODO("not implemented")
    }

    override fun AddToCart.tryIt() {
        return TODO("not implemented")
    }

    override fun CheckOut.tryIt() {
        return TODO("not implemented")
    }


}