package com.ubertob.pesticide.examples.petshop.http

import com.ubertob.kondor.json.*
import com.ubertob.kondor.json.jsonnode.JsonNodeObject
import com.ubertob.pesticide.examples.petshop.model.Cart
import com.ubertob.pesticide.examples.petshop.model.Pet


val jPetNames = JList(JString)

object JPet : JAny<Pet>() {

    val name by str(Pet::name)
    val price by num(Pet::price)

    override fun JsonNodeObject.deserializeOrThrow() =
        Pet(
            name = +name,
            price = +price
        )

}

object JCart : JAny<Cart>() {

    val id by num(Cart::id)
    val pets by array(JPet, Cart::pets)

    override fun JsonNodeObject.deserializeOrThrow() = Cart(
        id = +id,
        pets = +pets
    )

}