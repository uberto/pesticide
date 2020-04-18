package com.ubertob.pesticide


data class DdtStep<D : DomainUnderTest<*>>(val description: String, val action: (D) -> D)

interface DdtActor<D : DomainUnderTest<*>> {

    val name: String

    fun executeStep(block: D.() -> Unit): DdtStep<D> =
        executeStep(getCurrentMethodName(), block)

    private fun getCurrentMethodName() =
        Thread.currentThread().stackTrace[3].methodName //TODO needs a better way to find the exact stack trace relevant instead of just 3...

    fun executeStep(stepDesc: String, block: D.() -> Unit): DdtStep<D> =
        DdtStep(stepDesc) { it.also(block) }
}
