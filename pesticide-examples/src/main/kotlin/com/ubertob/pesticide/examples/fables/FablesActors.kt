package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.DdtActor

data class Human(override val name: String) : DdtActor<FablesDomainWrapper>() {
    fun `get basket with goods`(value: Int) = generateStep { TODO() }

    fun `go into the forest`() = generateStep { TODO() }

    fun `tell the GrandMa location to`(wolf: Wolf) = generateStep { TODO() }

    fun `go to GrandMa's house`() = generateStep { TODO() }

    fun `jump out belly of`(wolf: Wolf) = generateStep { TODO() }

    fun `receive the goods worth`(expectedValue: Int) = generateStep { TODO() }

    fun `cannot jump out belly of`(wolf: Wolf) = generateStep { TODO() }

    fun `cannot receive the goods`() = generateStep { TODO() }

}

data class Wolf(override val name: String) : DdtActor<FablesDomainWrapper>() {
    fun `meet and eat`(someone: Human) = generateStep { TODO() }

    fun `get killed by hunter`() = generateStep { TODO() }

    fun `cannot meet and eat`(human: Human) = generateStep { TODO() }

}

