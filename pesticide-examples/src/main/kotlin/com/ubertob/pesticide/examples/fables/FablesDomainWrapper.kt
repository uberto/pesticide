package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.DdtProtocol
import com.ubertob.pesticide.DomainUnderTest
import com.ubertob.pesticide.InMemoryHubs

class FablesDomainWrapper : DomainUnderTest<DdtProtocol> {
    override val protocol: DdtProtocol = InMemoryHubs

    lateinit var grandMaLocation: Location
    var wolfState: WolfState = WolfState.ignorant
    var girlLocation: Location = Location.littleRedRidingHoodHouse
    var basket: Basket? = null

    override fun isReady(): Boolean = true

    fun aGrandMaLivingAloneIntoTheForest(): FablesDomainWrapper =
        apply {
            grandMaLocation = Location.grandMaHouse
        }

    fun prepareBasket(value: Int, location: Location) =
        apply {
            basket = Basket(value, Location.littleRedRidingHoodHouse)
        }

    fun updateGirlLocation(location: FablesDomainWrapper.Location) =
        apply {
            girlLocation = location
        }

    fun updateWolfState(state: WolfState) =
        apply {
            wolfState = state
        }


    enum class Location {
        littleRedRidingHoodHouse, middleOfTheForest, grandMaHouse, insideTheWolfBelly
    }

    enum class WolfState {
        ignorant, knowAboutGrandMa, waitingForTheGirl, sleepy, dead
    }

    data class Basket(val value: Int, val location: FablesDomainWrapper.Location)
}

