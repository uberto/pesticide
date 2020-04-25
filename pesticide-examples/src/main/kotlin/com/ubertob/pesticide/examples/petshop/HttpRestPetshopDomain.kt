package com.ubertob.pesticide.examples.petshop

import com.ubertob.pesticide.PureHttp

class HttpRestPetshopDomain : PetShopDomain {
    override fun populateShop(vararg pets: Pet): PetShopDomain = TODO("not implemented")

    override fun PetShopAction.BuyPet.doIt(): PetShopDomain = TODO("not implemented")

    override fun PetShopAction.AskPrice.doIt(): PetShopDomain = TODO("not implemented")

    override val protocol = PureHttp("local")

    override fun isReady() = true //check on http server start


}