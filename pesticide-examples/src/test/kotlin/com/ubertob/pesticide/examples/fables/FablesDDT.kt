package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.DDT
import com.ubertob.pesticide.DomainDrivenTest
import com.ubertob.pesticide.NamedActor
import java.time.LocalDate

class FablesDDT : DomainDrivenTest<FablesDomainWrapper>(
    setOf(
        FablesDomainWrapper()
    )
) {

    val littleRedRidingHood by NamedActor(::Human)
    val bigBadWolf by NamedActor(::Wolf)
    val grandMa by NamedActor(::Human)

    @DDT
    fun `little red riding hood goes into the forest`() = ddtScenario {
//should we use 3rd person, imperative or "can xy" style?
        setting {
            aGrandMaLivingAloneIntoTheForest()
        } atRise play(
            littleRedRidingHood.`get basket with goods`(100),
            littleRedRidingHood.`go into the forest`(),
            littleRedRidingHood.`tell the GrandMa location to`(bigBadWolf),
            bigBadWolf.`meet and eat`(grandMa),
            littleRedRidingHood.`go to GrandMa's house`(),
            bigBadWolf.`meet and eat`(littleRedRidingHood),
            bigBadWolf.`get killed by hunter`(),
            littleRedRidingHood.`jump out belly of`(bigBadWolf),
            grandMa.`jump out belly of`(bigBadWolf),
            grandMa.`receive the goods worth`(100)
        ).wip(LocalDate.of(2020, 5, 30))
    }

    @DDT
    fun `smart girl scenario`() = ddtScenario {

        setting {
            aGrandMaLivingAloneIntoTheForest()
        } atRise play(
            littleRedRidingHood.`get basket with goods`(50),
            littleRedRidingHood.`go into the forest`(),
            //skip revealing the location to the wolf
            bigBadWolf.`cannot meet and eat`(grandMa),
            grandMa.`receive the goods worth`(50)
        ).wip(LocalDate.of(2020, 5, 30))
    }

    @DDT
    fun `wolf wins scenario`() = ddtScenario {
        setting {
            aGrandMaLivingAloneIntoTheForest()
        } atRise play(
            littleRedRidingHood.`get basket with goods`(100),
            littleRedRidingHood.`go into the forest`(),
            littleRedRidingHood.`tell the GrandMa location to`(bigBadWolf),
            bigBadWolf.`meet and eat`(grandMa),
            littleRedRidingHood.`go to GrandMa's house`(),
            bigBadWolf.`meet and eat`(littleRedRidingHood),
            //hunter forgot to kill the wolf
            littleRedRidingHood.`cannot jump out belly of`(bigBadWolf),
            grandMa.`cannot jump out belly of`(bigBadWolf),
            grandMa.`cannot receive the goods`()
        ).wip(LocalDate.of(2020, 5, 30))
    }
}