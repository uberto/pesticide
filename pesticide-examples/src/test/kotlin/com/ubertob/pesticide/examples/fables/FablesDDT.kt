package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.DDT
import com.ubertob.pesticide.DomainDrivenTest
import com.ubertob.pesticide.NamedActor

class FablesDDT : DomainDrivenTest<FablesDomainWrapper>(setOf(FablesDomainWrapper())) {

    val littleRedRidingHood by NamedActor(::Human)
    val bigBadWolf by NamedActor(::Wolf)

    @DDT
    fun `little red riding hood goes into the forest`() = ddtScenario {

        setting {
            aGrandMaLivingAloneIntoTheForest()
        } atRise play(
            littleRedRidingHood.`gets basket with goods worth $`(100),
            littleRedRidingHood.`goes into the forest`(),
            littleRedRidingHood.`tells the GrandMa location to Wolf`(),
            bigBadWolf.`goes to GrandMa's house`(),
            littleRedRidingHood.`goes to GrandMa's house`(),
            bigBadWolf.`meets and eats the girl`(),
            bigBadWolf.`got killed by hunter`(),
            littleRedRidingHood.`jumps out from the belly of Wolf`(),
            littleRedRidingHood.`gives to GrandMa the goods worth $`(100)
        )
    }

    @DDT
    fun `smart girl scenario`() = ddtScenario {

        setting {
            aGrandMaLivingAloneIntoTheForest()
        } atRise play(
            littleRedRidingHood.`gets basket with goods worth $`(50),
            littleRedRidingHood.`goes into the forest`(),
            //skip revealing the location to the wolf
            littleRedRidingHood.`goes to GrandMa's house`(),
            bigBadWolf.`cannot go to GrandMa's house`(),
            littleRedRidingHood.`gives to GrandMa the goods worth $`(50)
        )
    }

    @DDT
    fun `wolf wins scenario`() = ddtScenario {
        setting {
            aGrandMaLivingAloneIntoTheForest()
        } atRise play(
            littleRedRidingHood.`gets basket with goods worth $`(100),
            littleRedRidingHood.`goes into the forest`(),
            littleRedRidingHood.`tells the GrandMa location to Wolf`(),
            bigBadWolf.`goes to GrandMa's house`(),
            littleRedRidingHood.`goes to GrandMa's house`(),
            bigBadWolf.`meets and eats the girl`(),
            //hunter forgot to kill the wolf
            littleRedRidingHood.`cannot jump out belly of Wolf`(),
            littleRedRidingHood.`cannot give to GrandMa the goods`()
        )
    }
}