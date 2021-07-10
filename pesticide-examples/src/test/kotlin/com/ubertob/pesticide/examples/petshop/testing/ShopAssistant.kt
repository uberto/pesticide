package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.core.DdtActor
import com.ubertob.pesticide.examples.petshop.model.Pet
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.isNotNull

data class ShopAssistant(override val name: String) : DdtActor<PetShopSAInterpreter>() {

    fun `check that # is in the shop`(pet: Pet) = step(pet.name) {
        val pets = askPetList()
        expectThat(pets).isNotNull().contains(pet.name)
    }
}