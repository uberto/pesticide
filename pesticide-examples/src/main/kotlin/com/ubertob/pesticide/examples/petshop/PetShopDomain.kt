package com.ubertob.pesticide.examples.petshop

import com.ubertob.pesticide.DdtProtocol
import com.ubertob.pesticide.DomainCommand
import com.ubertob.pesticide.DomainQuery
import com.ubertob.pesticide.DomainUnderTest

fun allPetShopAbstractions() = setOf(
    InMemoryPetShopDomain(),
    HttpRestPetshopDomain()
)

interface PetShopDomain : DomainUnderTest<DdtProtocol> {
    fun populateShop(vararg pets: Pet): PetShopDomain

    fun PetShopAction.BuyPet.doIt(): PetShopDomain
    fun PetShopAction.AskPrice.doIt(): PetShopDomain

}

sealed class PetShopAction {
    data class BuyPet(val petName: String) : PetShopAction(), DomainCommand<PetShopDomain>
    data class AskPrice(val petName: String, override val verifyBlock: (Int) -> Unit) : PetShopAction(),
        DomainQuery<PetShopDomain, Int>
}

