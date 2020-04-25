package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.DDT
import com.ubertob.pesticide.DomainDrivenTest
import com.ubertob.pesticide.NamedActor
import java.time.LocalDate

class FablesDDT : DomainDrivenTest<FablesDomain>(
    setOf(
        FablesDomain()
    )
) {

    val littleRedRidingHood by NamedActor(::LittleGirl)
    val bigBadWolf by NamedActor(::Wolf)

    @DDT
    fun `little red riding hood goes into the forest`() = ddtScenario {

        setting {
            aGrandMaLivingAlone()
        } atRise play(
            littleRedRidingHood.`get basket with goods`(100),
            littleRedRidingHood.`go into the forest`(),
            littleRedRidingHood.`tell the GrandMa location to`(bigBadWolf),
            bigBadWolf.`goesAndEatGrandMa`(),
            littleRedRidingHood.`bring goods to GrandMa`(),
            bigBadWolf.`talk to and eat`(littleRedRidingHood),
            bigBadWolf.`get killed by hunter`(),
            littleRedRidingHood.`jump out belly of`(bigBadWolf)
        ).wip(LocalDate.of(2020, 5, 30))
    }
}