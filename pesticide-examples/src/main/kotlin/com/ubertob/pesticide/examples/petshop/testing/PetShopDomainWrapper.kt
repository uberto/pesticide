package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.DdtProtocol
import com.ubertob.pesticide.DomainCommand
import com.ubertob.pesticide.DomainQuery
import com.ubertob.pesticide.DomainUnderTest
import com.ubertob.pesticide.examples.petshop.model.Pet

fun allPetShopAbstractions() = setOf(
    InMemoryPetShopDomain(),
    HttpRestPetshopDomain("localhost", 8082)
)

interface PetShopDomainWrapper : DomainUnderTest<DdtProtocol> {
    fun populateShop(vararg pets: Pet): PetShopDomainWrapper

    fun BuyPet.tryIt(): PetShopDomainWrapper
    fun PetPrice.askIt(): PetShopDomainWrapper

}

data class BuyPet(val petName: String) : DomainCommand<PetShopDomainWrapper>
data class PetPrice(val petName: String, override val verifyBlock: (Int?) -> Unit) :
    DomainQuery<PetShopDomainWrapper, Int>


