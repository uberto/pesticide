package com.ubertob.pesticide.core

data class Setting<D : DomainInterpreter<*>>(private val block: StepBlock<D, Unit>? = null) {
    private val fakeActor = FakeActor<D>()

    fun asStep() =
        if (block != null)
            DdtStep(fakeActor, "Setting up the scenario", block)
        else
            DdtStep(fakeActor, "Empty scenario", {})

}

class FakeActor<D : DomainInterpreter<*>> : DdtActor<D>() {
    override val name: String = "Fake"
}

