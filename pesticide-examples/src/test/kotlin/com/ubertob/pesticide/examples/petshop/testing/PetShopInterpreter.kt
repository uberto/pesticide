package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.DdtProtocol
import com.ubertob.pesticide.DomainInterpreter
import com.ubertob.pesticide.DomainQuery
import com.ubertob.pesticide.examples.petshop.model.Pet

interface PetShopInterpreter : DomainInterpreter<DdtProtocol> {

    fun populateShop(vararg pets: Pet): PetShopInterpreter
//    fun BuyPet.tryIt()

    fun PetPrice.askIt()
    fun PetList.askIt()
}

val allPetShopAbstractions = setOf(
    InMemoryPetShop(),
    HttpRestPetshopDomain("localhost", 8082)
)

//data class BuyPet(val petName: String) : DomainCommand<PetShopInterpreter>
data class PetPrice(val petName: String, override val verifyBlock: (Int?) -> Unit) :
    DomainQuery<PetShopInterpreter, Int>

data class PetList(override val verifyBlock: (List<String>?) -> Unit) :
    DomainQuery<PetShopInterpreter, List<String>>

