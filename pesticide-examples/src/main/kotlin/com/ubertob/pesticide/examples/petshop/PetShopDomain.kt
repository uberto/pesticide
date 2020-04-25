package com.ubertob.pesticide.examples.petshop

import com.ubertob.pesticide.*
import com.ubertob.pesticide.examples.petshop.PetShopDomain.PetShopAction

fun allPetShopAbstractions() = setOf(
    InMemoryPetShopDomain(),
    HttpRestPetshopDomain()
)

interface PetShopDomain : DomainUnderTest<DdtProtocol> {
    fun populateShop(vararg pets: Pet): PetShopDomain {
        TODO("populate shop")
    }

    fun PetShopAction.BuyPet.doIt(): PetShopDomain
    fun PetShopAction.AskPrice.doIt(): PetShopDomain

    sealed class PetShopAction {
        data class BuyPet(val petName: String) : PetShopAction(), DomainCommand<PetShopDomain>
        data class AskPrice(val petName: String, override val verifyBlock: (Int) -> Unit) : PetShopAction(),
            DomainQuery<PetShopDomain, Int>
    }
}

data class InMemoryPetShopDomain(val pets: List<Pet> = emptyList()) : PetShopDomain {


    override fun PetShopAction.BuyPet.doIt() =
        this@InMemoryPetShopDomain.copy(pets.filterNot { it.name == petName })

    override fun PetShopAction.AskPrice.doIt() =
        this@InMemoryPetShopDomain.also {
            verifyBlock(pets.first { it.name == petName }.price)
        }

    override val protocol = InMemoryHubs

    override fun isReady() = true


}

class HttpRestPetshopDomain : PetShopDomain {
    override fun PetShopAction.BuyPet.doIt(): PetShopDomain = TODO("not implemented")

    override fun PetShopAction.AskPrice.doIt(): PetShopDomain = TODO("not implemented")

    override val protocol = PureHttp("local")

    override fun isReady() = true //check on http server start


}

