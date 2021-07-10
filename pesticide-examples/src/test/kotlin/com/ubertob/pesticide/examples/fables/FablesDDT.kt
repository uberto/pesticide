package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.core.DDT
import com.ubertob.pesticide.core.DomainDrivenTest

class FablesDDT : DomainDrivenTest<FablesInterpreter>(setOf(FablesInterpreter())) {
    val littleRedRidingHood by NamedActor(::Human)
    val bigBadWolf by NamedActor(::Wolf)

    @DDT
    fun `little red riding hood goes into the forest`() = ddtScenario {

        setUp {
            aGrandMaLivingAloneIntoTheForest()
        }.thenPlay(
            littleRedRidingHood.`gets basket with goods worth #`(100),
            littleRedRidingHood.`goes into the forest`(),
            bigBadWolf.`get GrandMa location from #`(littleRedRidingHood),
            bigBadWolf.`goes to GrandMa's house`(),
            littleRedRidingHood.`goes to GrandMa's house`(),
            bigBadWolf.`meets and eats the girl`(),
            bigBadWolf.`got killed by hunter`(),
            littleRedRidingHood.`jumps out from the belly of Wolf`(),
            littleRedRidingHood.`gives to GrandMa the goods worth #`(100)
        )
    }

    @DDT
    fun `smart girl scenario`() = ddtScenario {

        setUp {
            aGrandMaLivingAloneIntoTheForest()
        }.thenPlay(
            littleRedRidingHood.`gets basket with goods worth #`(50),
            littleRedRidingHood.`goes into the forest`(),
            //skip revealing the location to the wolf
            littleRedRidingHood.`goes to GrandMa's house`(),
            bigBadWolf.`cannot go to GrandMa's house`(),
            littleRedRidingHood.`gives to GrandMa the goods worth #`(50)
        )
    }

    @DDT
    fun `wolf wins scenario`() = ddtScenario {
        setUp {
            aGrandMaLivingAloneIntoTheForest()
        }.thenPlay(
            littleRedRidingHood.`gets basket with goods worth #`(100),
            littleRedRidingHood.`goes into the forest`(),
            bigBadWolf.`get GrandMa location from #`(littleRedRidingHood),
            bigBadWolf.`goes to GrandMa's house`(),
            littleRedRidingHood.`goes to GrandMa's house`(),
            bigBadWolf.`meets and eats the girl`(),
            //hunter forgot to kill the wolf
            littleRedRidingHood.`cannot jump out belly of Wolf`(),
            littleRedRidingHood.`cannot give to GrandMa the goods`()
        )
    }
}