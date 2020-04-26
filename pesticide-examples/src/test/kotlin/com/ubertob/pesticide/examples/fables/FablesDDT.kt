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

    val littleRedRidingHood by NamedActor(::Human)
    val bigBadWolf by NamedActor(::Wolf)
    val grandMa by NamedActor(::Human)

    @DDT
    fun `little red riding hood goes into the forest`() = ddtScenario {

        setting {
            aGrandMaLivingAloneIntoTheForest()
        } atRise play(
            littleRedRidingHood.`get basket with goods`(100),
            littleRedRidingHood.`go into the forest`(),
            littleRedRidingHood.`tell the GrandMa location to`(bigBadWolf),
            bigBadWolf.`meet and eat`(grandMa),
            littleRedRidingHood.`goes to GrandMa's house`(),
            bigBadWolf.`meet and eat`(littleRedRidingHood),
            bigBadWolf.`get killed by hunter`(),
            littleRedRidingHood.`jump out belly of`(bigBadWolf),
            grandMa.`jump out belly of`(bigBadWolf),
            littleRedRidingHood.`delivey goods to`(grandMa, 100)
        ).wip(LocalDate.of(2020, 5, 30))
    }
}