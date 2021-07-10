package com.ubertob.pesticide.core

data class DdtSetup<D : DdtActions<*>>(private val block: StepBlock<D, Unit>? = null) {

    fun asStep() =
        if (block != null)
            DdtStep("settings", "Setting up the scenario", block)
        else
            DdtStep("settings", "No SetUp", {})

}

