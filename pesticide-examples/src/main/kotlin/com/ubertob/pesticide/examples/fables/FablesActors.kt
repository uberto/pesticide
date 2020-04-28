package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.DdtActor
import com.ubertob.pesticide.DdtStep

data class Human(override val name: String) : DdtActor<FablesDomain>() {
    fun `get basket with goods`(value: Int): DdtStep<FablesDomain> = generateStep { TODO() }

    fun `go into the forest`(): DdtStep<FablesDomain> = generateStep { TODO() }

    fun `tell the GrandMa location to`(wolf: Wolf): DdtStep<FablesDomain> = generateStep { TODO() }

    fun `go to GrandMa's house`(): DdtStep<FablesDomain> = generateStep { TODO() }

    fun `jump out belly of`(wolf: Wolf): DdtStep<FablesDomain> = generateStep { TODO() }

    fun `receive the goods worth`(expectedValue: Int): DdtStep<FablesDomain> = generateStep { TODO() }

    fun `cannot jump out belly of`(wolf: Wolf): DdtStep<FablesDomain> = generateStep { TODO() }

    fun `cannot receive the goods`(): DdtStep<FablesDomain> = generateStep { TODO() }

}

data class Wolf(override val name: String) : DdtActor<FablesDomain>() {
    fun `meet and eat`(someone: Human): DdtStep<FablesDomain> = generateStep { TODO() }

    fun `get killed by hunter`(): DdtStep<FablesDomain> = generateStep { TODO() }

    fun `cannot meet and eat`(human: Human): DdtStep<FablesDomain> = generateStep { TODO() }

}

