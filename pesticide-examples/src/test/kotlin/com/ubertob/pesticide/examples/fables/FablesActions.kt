package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.core.*

class FablesActions : DomainActions<DdtProtocol> {
    override val protocol: DdtProtocol =
        DomainOnly

    lateinit var grandMaLocation: Location
    var wolfState: WolfState =
        WolfState.ignorant
    var girlLocation: Location =
        Location.littleRedRidingHoodHouse
    var basket: Basket? = null

    override fun prepare(): DomainSetUp =
        Ready

    fun aGrandMaLivingAloneIntoTheForest(): FablesActions =
        apply {
            grandMaLocation = Location.grandMaHouse
        }

    fun Human.prepareBasket(value: Int, location: Location) {
        basket = Basket(value, location)
    }

    fun Human.updateGirlLocation(location: Location) {
        girlLocation = location
    }

    fun Wolf.eatGirl() {
        girlLocation = Location.insideTheWolfBelly
    }

    fun Wolf.updateWolfState(state: WolfState) {
        wolfState = state
    }


}

