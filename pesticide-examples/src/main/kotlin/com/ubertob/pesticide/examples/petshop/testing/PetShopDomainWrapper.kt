package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.DdtProtocol
import com.ubertob.pesticide.DomainCommand
import com.ubertob.pesticide.DomainQuery
import com.ubertob.pesticide.DomainUnderTest
import com.ubertob.pesticide.examples.petshop.model.Pet

fun allPetShopAbstractions() = setOf(
    InMemoryPetShopDomain(),
    HttpRestPetshopDomain()
)

interface PetShopDomainWrapper : DomainUnderTest<DdtProtocol> {
    fun populateShop(vararg pets: Pet): PetShopDomainWrapper

    fun BuyPet.doIt(): PetShopDomainWrapper
    fun AskPrice.doIt(): PetShopDomainWrapper

}

sealed class PetShopAction
data class BuyPet(val petName: String) : PetShopAction(), DomainCommand<PetShopDomainWrapper>
data class AskPrice(val petName: String, override val verifyBlock: (Int?) -> Unit) : PetShopAction(),
    DomainQuery<PetShopDomainWrapper, Int>


