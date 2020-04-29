package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.DdtActor

data class Human(override val name: String) : DdtActor<FablesDomainWrapper>() {
    fun `get basket with goods`(value: Int) = generateStepName { TODO() }

    fun `go into the forest`() = generateStepName { TODO() }

    fun `tell the GrandMa location to`(wolf: Wolf) = generateStepName { TODO() }

    fun `go to GrandMa's house`() = generateStepName { TODO() }

    fun `jump out belly of`(wolf: Wolf) = generateStepName { TODO() }

    fun `receive the goods worth`(expectedValue: Int) = generateStepName { TODO() }

    fun `cannot jump out belly of`(wolf: Wolf) = generateStepName { TODO() }

    fun `cannot receive the goods`() = generateStepName { TODO() }

}

data class Wolf(override val name: String) : DdtActor<FablesDomainWrapper>() {
    fun `meet and eat`(someone: Human) = generateStepName { TODO() }

    fun `get killed by hunter`() = generateStepName { TODO() }

    fun `cannot meet and eat`(human: Human) = generateStepName { TODO() }

}

