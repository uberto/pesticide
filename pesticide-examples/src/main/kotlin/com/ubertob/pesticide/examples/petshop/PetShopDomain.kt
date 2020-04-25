package com.ubertob.pesticide.examples.petshop

import com.ubertob.pesticide.DdtProtocol
import com.ubertob.pesticide.DomainUnderTest
import com.ubertob.pesticide.InMemoryHubs
import com.ubertob.pesticide.PureHttp

fun allPetShopAbstractions() = setOf(
    InMemoryPetshopDomain(),
    HttpRestPetshopDomain()
)

interface PetShopDomain : DomainUnderTest<DdtProtocol> {
    fun populateShop(vararg pets: Pet): PetShopDomain {
        TODO("populate shop")
    }


}

class InMemoryPetshopDomain : PetShopDomain {

    override val protocol = InMemoryHubs

    override fun isReady() = true

}

class HttpRestPetshopDomain : PetShopDomain {

    override val protocol = PureHttp("local")

    override fun isReady() = true //check on http server start

}

