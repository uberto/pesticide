package com.ubertob.pesticide.core

data class Setting<D : DomainInterpreter<*>>(private val block: StepBlock<D, Unit>? = null) {

    fun asStep() =
        if (block != null)
            DdtStep("settings", "Setting up the scenario", block)
        else
            DdtStep("settings", "Empty scenario", {})

}

