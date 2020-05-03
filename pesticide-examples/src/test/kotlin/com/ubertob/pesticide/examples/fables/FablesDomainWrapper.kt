package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.*

class FablesDomainWrapper : DomainUnderTest<DdtProtocol> {
    override val protocol: DdtProtocol = InMemoryHubs

    lateinit var grandMaLocation: Location
    var wolfState: WolfState =
        WolfState.ignorant
    var girlLocation: Location =
        Location.littleRedRidingHoodHouse
    var basket: Basket? = null

    override fun prepare(): DomainSetUp = Ready

    fun aGrandMaLivingAloneIntoTheForest(): FablesDomainWrapper =
        apply {
            grandMaLocation = Location.grandMaHouse
        }

    fun prepareBasket(value: Int, location: Location) =
        apply {
            basket = Basket(value, location)
        }

    fun updateGirlLocation(location: Location) =
        apply {
            girlLocation = location
        }

    fun updateWolfState(state: WolfState) =
        apply {
            wolfState = state
        }


}

