package com.ubertob.pesticide.examples.petshop.testing

import com.ubertob.pesticide.DomainSetUp
import com.ubertob.pesticide.PureHttp
import com.ubertob.pesticide.Ready
import com.ubertob.pesticide.examples.petshop.model.Pet

class HttpRestPetshopDomain : PetShopDomainWrapper {
    override fun populateShop(vararg pets: Pet): PetShopDomainWrapper = TODO("not implemented")

    override fun BuyPet.doIt(): PetShopDomainWrapper = TODO("not implemented")

    override fun AskPrice.doIt(): PetShopDomainWrapper = TODO("not implemented")

    override val protocol = PureHttp("local")

    override fun prepare(): DomainSetUp = Ready //check on http server start


}