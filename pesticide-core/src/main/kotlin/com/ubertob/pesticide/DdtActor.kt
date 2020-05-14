package com.ubertob.pesticide

import java.util.function.Consumer


abstract class DdtActor<D : DomainInterpreter<*>> : DdtActorWithContext<D, Unit>() {

    fun stepWithDesc(
        stepDesc: String,
        block: Consumer<D>
    ): DdtStep<D, Unit> =
        stepWithDesc(stepDesc) {
            block.accept(this)
            Unit
        }
}

data class StepContext<C>(val context: C?, private val contextUpdater: (C?) -> Unit) {
    fun updateContext(newContext: C) = contextUpdater(newContext)
    fun deleteContext() = contextUpdater(null)
}

typealias StepBlock<D, C> = D.(StepContext<C>) -> Unit

abstract class DdtActorWithContext<D : DomainInterpreter<*>, C : Any> {

    abstract val name: String

    private fun getCurrentMethodName() =
        Thread.currentThread().stackTrace[4].methodName //TODO needs a better way to find the exact stack trace relevant instead of just 3...


    fun step(vararg parameters: Any, block: StepBlock<D, C>): DdtStep<D, C> =
        stepWithDesc(generateStepName(parameters), block)

    fun step(block: StepBlock<D, C>): DdtStep<D, C> =
        stepWithDesc(generateStepName(), block)

    private fun generateStepName() =
        "$name ${getCurrentMethodName()}" //TODO in case of camel notation or snake notation decode the method name

    private fun generateStepName(parameters: Array<out Any>) =
        "$name ${getCurrentMethodName()}".replaceDollars(parameters.map { it.toString() })


    fun stepWithDesc(stepDesc: String, block: StepBlock<D, C>): DdtStep<D, C> =
        DdtStep(this, stepDesc, block)
}

private fun String.replaceDollars(parameters: List<String>): String = parameters
    .fold(this) { text, param ->
        text.replaceFirst("$", param)
    }
