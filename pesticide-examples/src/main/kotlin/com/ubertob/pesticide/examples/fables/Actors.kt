package com.ubertob.pesticide.examples.fables

import com.ubertob.pesticide.DdtActor
import com.ubertob.pesticide.DdtStep

data class Human(override val name: String) : DdtActor<FablesDomain>() {
    fun `get basket with goods`(value: Int): DdtStep<FablesDomain> {
        return TODO("not implemented")
    }

    fun `go into the forest`(): DdtStep<FablesDomain> {
        return TODO("not implemented")
    }

    fun `tell the GrandMa location to`(wolf: Wolf): DdtStep<FablesDomain> {
        return TODO("not implemented")
    }

    fun `go to GrandMa's house`(): DdtStep<FablesDomain> {
        TODO("not implemented")
    }

    fun `jump out belly of`(wolf: Wolf): DdtStep<FablesDomain> {
        TODO("not implemented")
    }

    fun `receive the goods worth`(expectedValue: Int): DdtStep<FablesDomain> {
        return TODO("not implemented")
    }

    fun `cannot jump out belly of`(wolf: Wolf): DdtStep<FablesDomain> {
        return TODO("not implemented")
    }

    fun `cannot receive the goods`(): DdtStep<FablesDomain> {

    }

}

data class Wolf(override val name: String) : DdtActor<FablesDomain>() {
    fun `meet and eat`(someone: Human): DdtStep<FablesDomain> {
        return TODO("not implemented")
    }

    fun `get killed by hunter`(): DdtStep<FablesDomain> {
        TODO("not implemented")
    }

    fun `cannot meet and eat`(human: Human): DdtStep<FablesDomain> {
        return TODO("not implemented")
    }

}

