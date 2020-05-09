package com.ubertob.pesticide.examples.petshop.model

import java.util.concurrent.atomic.AtomicReference
import kotlin.random.Random

class PetShopHub() {

    private val pets: AtomicReference<List<Pet>> = AtomicReference(emptyList())
    private val carts: AtomicReference<List<Cart>> = AtomicReference(emptyList())

    fun addPet(pet: Pet): List<Pet> = pets.updateAndGet {
        it + pet
    }

    fun getByName(petName: String): Pet? = pets.get().firstOrNull { it.name == petName }

    fun getAll(): List<String> = pets.get().map { it.name }

    fun createCart(): Cart = newCart().apply { carts.updateAndGet { it + this } }

    fun getCart(cartId: Int) = carts.get().firstOrNull { it.id == cartId }

    fun addPetToCart(cartId: Int, petName: String): Cart? {
        val pet = getByName(petName)
        val cart = getCart(cartId)
        return if (pet != null && cart != null) {
            val newCart = cart.copy(pets = cart.pets + pet)
            pets.updateAndGet { it.filterNot { it.name == petName } }
            carts.updateAndGet {
                it.filterNot { it.id == cartId } + newCart
            }
            newCart
        } else null
    }

    fun cartCheckout(cartId: Int): Cart? =
        getCart(cartId).apply {
            carts.updateAndGet { it.filterNot { it.id == cartId } }
        }


    private fun newCart() =
        Cart(Random.nextInt(10000), emptyList())


}
