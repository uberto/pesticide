package com.ubertob.pesticide.examples.petshop.model

data class Cart(val id: Int, val pets: List<Pet>) {
    fun total(): Int = pets.sumBy { it.price }
}