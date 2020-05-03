package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.DdtActor
import com.ubertob.pesticide.examples.fables.Location.*
import com.ubertob.pesticide.examples.fables.WolfState.*
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo

data class Human(override val name: String) : DdtActor<FablesDomainWrapper>() {
    fun `gets basket with goods worth $`(value: Int) = step(value.toString()) {
        prepareBasket(value, littleRedRidingHoodHouse)
    }

    fun `goes into the forest`() = step {
        updateGirlLocation(middleOfTheForest)
    }


    fun `tells the GrandMa location to Wolf`() = step {
        expectThat(girlLocation).isEqualTo(middleOfTheForest)
        updateWolfState(knowAboutGrandMa)
    }

    fun `goes to GrandMa's house`() = step {
        updateGirlLocation(grandMaHouse)
    }

    fun `jumps out from the belly of Wolf`() = step {
        expectThat(wolfState).isEqualTo(dead)
        expectThat(girlLocation).isEqualTo(insideTheWolfBelly)
        updateGirlLocation(grandMaHouse)
    }

    fun `gives to GrandMa the goods worth $`(expectedValue: Int) = step(expectedValue.toString()) {
        expectThat(girlLocation).isEqualTo(grandMaHouse)
        basket = basket?.copy(location = grandMaLocation)
        expectThat(basket?.value).isEqualTo(expectedValue)
    }

    fun `cannot jump out belly of Wolf`() = step {
        expectThat(wolfState).isNotEqualTo(dead)
    }

    fun `cannot give to GrandMa the goods`() = step {
        expectThat(girlLocation).isNotEqualTo(grandMaHouse)
    }

}

data class Wolf(override val name: String) : DdtActor<FablesDomainWrapper>() {
    fun `meets and eats the girl`() = step {
        expectThat(girlLocation).isEqualTo(grandMaHouse)
        expectThat(wolfState).isEqualTo(waitingForTheGirl)
        updateGirlLocation(insideTheWolfBelly)
        updateWolfState(sleepy)
    }

    fun `got killed by hunter`() = step {
        expectThat(wolfState).isEqualTo(sleepy)
        updateWolfState(dead)
    }

    fun `cannot go to GrandMa's house`() = step {
        expectThat(wolfState).isEqualTo(ignorant)
    }

    fun `goes to GrandMa's house`() = step(name) {
        expectThat(wolfState).isEqualTo(knowAboutGrandMa)
        updateWolfState(waitingForTheGirl)
    }

}

