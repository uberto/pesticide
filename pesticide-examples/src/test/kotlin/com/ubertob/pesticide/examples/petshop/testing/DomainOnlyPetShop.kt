package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.core.DomainOnly
import com.ubertob.pesticide.core.DomainSetUp
import com.ubertob.pesticide.core.Ready
import com.ubertob.pesticide.examples.petshop.model.Pet
import com.ubertob.pesticide.examples.petshop.model.PetShopHub

class DomainOnlyPetShop() : PetShopInterpreter {

    private val hub = PetShopHub()

    override val protocol = DomainOnly

    override fun prepare(): DomainSetUp =
        Ready

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
        verifyBlock(hub.getCart(cartId))
    }

    override fun NewCart.createIt(): CartId? =
        hub.createCart().id

    override fun AddToCart.tryIt() {
        hub.addPetToCart(cartId, petName)
    }

    override fun CheckOut.tryIt() {
        hub.cartCheckout(cartId)
    }


}