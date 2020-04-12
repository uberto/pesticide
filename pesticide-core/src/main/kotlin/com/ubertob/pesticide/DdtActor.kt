package com.ubertob.pesticide


data class DdtStep<D : DomainUnderTest<*>>(val description: String, val runnable: (D) -> D)

interface DdtActor<D : DomainUnderTest<*>> {

    val name: String

    fun executeStep(block: D.() -> Unit): DdtStep<D> =
        executeStep(getCurrentMethodName(), block)

    private fun getCurrentMethodName() = Thread.currentThread().stackTrace[3].methodName

    fun executeStep(stepDesc: String, block: D.() -> Unit): DdtStep<D> =
        DdtStep(stepDesc) { it.also(block) }
}
