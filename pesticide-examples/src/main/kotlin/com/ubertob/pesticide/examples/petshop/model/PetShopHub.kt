package com.ubertob.pesticide.examples.petshop.model

import java.util.concurrent.atomic.AtomicReference

class PetShopHub() {

    private val pets: AtomicReference<List<Pet>> = AtomicReference(emptyList())

    fun addPet(pet: Pet): List<Pet> = pets.updateAndGet {
        it + pet
    }

    fun buyPet(petName: String): List<Pet> = pets.updateAndGet { it.filterNot { it.name == petName } }
    fun getByName(petName: String): Pet? = pets.get().firstOrNull { it.name == petName }
}
