package com.ubertob.pesticide.core

import java.util.function.Consumer

/**
 * DdtActor is the base class to inherit from if we don't need a context.
 *
 * see  {@link DdtActorWithContext} if you need a context
 *
 * users are generally created with the NamedActor delegate in the DomainDrivenTest
 *
 */
abstract class DdtActor<ACTIONS : DdtActions<*>> : DdtActorWithContext<ACTIONS, Unit>() {

    @JvmName("stepWithDesc")
    fun stepWithDescJava(
        stepDesc: String,
        block: Consumer<ACTIONS>
    ): DdtStep<ACTIONS, Unit> =
        stepWithDesc(stepDesc) {
            block.accept(this)
        }
}


typealias StepBlock<D, C> = D.(StepContext<C>) -> Unit

/**
 * DdtActorWithContext is the base class to inherit from we need a context to store and retrieve information during our tests.
 *
 * An User should have a list of methods calling step in order to be used inside a DDT.
 *
 * <pre>
 *     fun `eating $ now`(foodName: String) = step(foodName){ ... }
 * </pre>
 *
 * see  {@link DdtActor} if you don't need a context
 *
 */
abstract class DdtActorWithContext<ACTIONS : DdtActions<*>, CTX : Any> {

    abstract val name: String

    private fun getCurrentMethodName() =
        Thread.currentThread().stackTrace[4].methodName //TODO needs a better way to find the exact stack trace...

    internal fun generateStepName() =
        "$name ${getCurrentMethodName()}" //TODO in case of camel notation or snake notation decode the method name

    internal fun generateStepName(parameters: Array<out Any>) =
        "$name ${getCurrentMethodName()}".replaceParams(parameters.map { it.toString() })


    fun step(vararg parameters: Any, block: StepBlock<ACTIONS, CTX>): DdtStep<ACTIONS, CTX> =
        stepWithDesc(generateStepName(parameters), block)

    fun step(block: StepBlock<ACTIONS, CTX>): DdtStep<ACTIONS, CTX> =
        stepWithDesc(generateStepName(), block)

    protected fun stepWithDesc(stepDesc: String, block: StepBlock<ACTIONS, CTX>): DdtStep<ACTIONS, CTX> =
        DdtStep(name, stepDesc, block)

}


private fun String.replaceWildcard(wildcard: String, parameters: List<String>): String =
    split("\\s+".toRegex()).map { word ->
        word.replace("""^[,\.]|[,\.]$""".toRegex(), "")
    }.filter { it.startsWith("#") }
        .zip(parameters)
        .fold(this) { text, pp ->
            text.replaceFirst(pp.first, pp.second)
        }

private fun String.replaceParams(parameters: List<String>): String =
    replaceWildcard("$", parameters)
        .replaceWildcard("#", parameters)
