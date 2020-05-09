package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.DdtProtocol
import com.ubertob.pesticide.DomainCommand
import com.ubertob.pesticide.DomainInterpreter
import com.ubertob.pesticide.DomainQuery
import com.ubertob.pesticide.examples.petshop.model.Cart
import com.ubertob.pesticide.examples.petshop.model.Pet

interface PetShopInterpreter : DomainInterpreter<DdtProtocol> {

    fun populateShop(vararg pets: Pet): PetShopInterpreter

    fun PetPrice.askIt()
    fun PetList.askIt()
    fun CartStatus.askIt()

    fun NewCart.createIt(): CartId?
    fun AddToCart.tryIt()
    fun CheckOut.tryIt()
}

val allPetShopAbstractions = setOf(
    InMemoryPetShop(),
    HttpRestPetshop("localhost", 8082)
)

object NewCart : DomainCommand<PetShopInterpreter>
data class AddToCart(val cartId: CartId, val petName: String) : DomainCommand<PetShopInterpreter>
data class CheckOut(val cartId: CartId) : DomainCommand<PetShopInterpreter>


data class PetPrice(val petName: String, override val verifyBlock: (Int?) -> Unit) :
    DomainQuery<PetShopInterpreter, Int>

data class PetList(override val verifyBlock: (List<String>?) -> Unit) :
    DomainQuery<PetShopInterpreter, List<String>>

data class CartStatus(val cartId: CartId, override val verifyBlock: (Cart?) -> Unit) :
    DomainQuery<PetShopInterpreter, Cart>


