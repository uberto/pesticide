package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.core.*

class FablesInterpreter : DomainInterpreter<DdtProtocol> {
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

    fun aGrandMaLivingAloneIntoTheForest(): FablesInterpreter =
        apply {
            grandMaLocation = Location.grandMaHouse
        }

    fun Human.prepareBasket(value: Int, location: Location) {
        basket = Basket(value, location)
    }

    fun Human.updateGirlLocation(location: Location) {
        girlLocation = location
    }

    fun Human.tellTheWolf() {
        wolfState = WolfState.knowAboutGrandMa
    }

    fun Wolf.eatGirl() {
        girlLocation = Location.insideTheWolfBelly
    }

    fun Wolf.updateWolfState(state: WolfState) {
        wolfState = state
    }


}

