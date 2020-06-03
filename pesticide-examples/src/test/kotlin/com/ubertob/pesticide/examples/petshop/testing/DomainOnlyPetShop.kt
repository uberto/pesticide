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

    override fun populateShop(vararg pets: Pet) =
        pets.forEach {
            hub.addPet(it)
        }


    override fun askPetPrice(petName: String): Int? = hub.getByName(petName)?.price

    override fun askPetList() = hub.getAll()

    override fun askCartStatus(cartId: CartId) = hub.getCart(cartId)

    override fun createNewCart(): CartId? =
        hub.createCart().id

    override fun addToCart(cartId: CartId, petName: String) {
        hub.addPetToCart(cartId, petName)
    }

    override fun checkOut(cartId: CartId) {
        hub.cartCheckout(cartId)
    }


}