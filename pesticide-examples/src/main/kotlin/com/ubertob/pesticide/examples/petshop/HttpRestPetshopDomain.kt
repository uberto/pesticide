package com.ubertob.pesticide.examples.petshop

import com.ubertob.pesticide.DomainSetUp
import com.ubertob.pesticide.PureHttp
import com.ubertob.pesticide.Ready

class HttpRestPetshopDomain : PetShopDomainWrapper {
    override fun populateShop(vararg pets: Pet): PetShopDomainWrapper = TODO("not implemented")

    override fun PetShopAction.BuyPet.doIt(): PetShopDomainWrapper = TODO("not implemented")

    override fun PetShopAction.AskPrice.doIt(): PetShopDomainWrapper = TODO("not implemented")

    override val protocol = PureHttp("local")

    override fun prepare(): DomainSetUp = Ready //check on http server start


}