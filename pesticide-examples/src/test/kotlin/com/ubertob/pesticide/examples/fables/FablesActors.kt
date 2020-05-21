package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.core.DdtActorWithContext
import com.ubertob.pesticide.examples.fables.Location.*
import com.ubertob.pesticide.examples.fables.WolfState.*
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo

data class Human(override val name: String) : DdtActorWithContext<FablesInterpreter, String>() {
    fun `gets basket with goods worth $`(value: Int) = step(value.toString()) { ctx ->
        prepareBasket(value, littleRedRidingHoodHouse)
        ctx.store("GrandMa's secret address")
    }

    fun `goes into the forest`() = step {
        updateGirlLocation(middleOfTheForest)
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

data class Wolf(override val name: String) : DdtActorWithContext<FablesInterpreter, String>() {
    fun `meets and eats the girl`() = step {
        expectThat(girlLocation).isEqualTo(grandMaHouse)
        expectThat(wolfState).isEqualTo(waitingForTheGirl)
        eatGirl()
        updateWolfState(sleepy)
    }

    fun `got killed by hunter`() = step {
        expectThat(wolfState).isEqualTo(sleepy)
        updateWolfState(dead)
    }

    fun `cannot go to GrandMa's house`() = step {
        expectThat(wolfState).isEqualTo(ignorant)
    }

    fun `goes to GrandMa's house`() = step(name) { ctx ->
        expectThat(ctx.get()).isEqualTo("GrandMa's secret address")
        updateWolfState(waitingForTheGirl)
    }

    fun `get GrandMa location from $`(human: Human) = step(human.name) { ctx ->
        ctx.getFrom(human) { it }
    }

}

