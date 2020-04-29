package com.ubertob.pesticide.examples.petshop

import com.ubertob.pesticide.PureHttp

class HttpRestPetshopDomain : PetShopDomainWrapper {
    override fun populateShop(vararg pets: Pet): PetShopDomainWrapper = TODO("not implemented")

    override fun PetShopAction.BuyPet.doIt(): PetShopDomainWrapper = TODO("not implemented")

    override fun PetShopAction.AskPrice.doIt(): PetShopDomainWrapper = TODO("not implemented")

    override val protocol = PureHttp("local")

    override fun isReady() = true //check on http server start


}