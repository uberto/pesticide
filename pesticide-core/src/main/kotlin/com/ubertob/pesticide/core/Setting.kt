package com.ubertob.pesticide.core

data class Setting<D : DomainInterpreter<*>>(val block: StepBlock<D, Unit>? = null) {
    private val fakeActor = FakeActor<D>()

    val step = block
        ?.let { DdtStep(fakeActor, "Setting up the scenario", it) }
        ?: DdtStep(fakeActor, "Empty scenario") {}

}

class FakeActor<D : DomainInterpreter<*>> : DdtActor<D>() {
    override val name: String = "Fake"
}

