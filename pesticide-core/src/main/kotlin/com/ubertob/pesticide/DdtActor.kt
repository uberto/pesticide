package com.ubertob.pesticide

import java.io.File
import java.net.URI
import java.util.function.Consumer


data class DdtStep<D : DomainInterpreter<*>, C : Any>(
    val actor: DdtActorWithContext<D, C>,
    val description: String,
    val action: StepBlock<D, C>
) {
    val stackTraceElement = Thread.currentThread().stackTrace[5]

    fun testSourceURI(): URI? =
        stackTraceElement?.toSourceReference(sourceRoot) //TODO can we guess better the sourceRoot?

    fun StackTraceElement.toSourceReference(sourceRoot: File): URI? {
        val fileName = fileName ?: return null
        val type = Class.forName(className)

        val pathpesticide =
            sourceRoot.toPath().resolve(type.`package`.name.replace(".", "/")).resolve(fileName).toFile().absolutePath

        return URI("file://$pathpesticide?line=$lineNumber")

    }

}

private val sourceRoot = listOf(
    File("src/test/kotlin"),
    File("src/test/java")
).find { it.isDirectory } ?: File(".")  //TODO make sure it works for others as well

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
    fun updateContext(newContext: C?) = contextUpdater(newContext)
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
        "$name ${getCurrentMethodName()}" //TODO in case of camel notation or snake notation decode the meethod name

    private fun generateStepName(parameters: Array<out Any>) =
        "$name ${getCurrentMethodName()}".replaceDollars(parameters.map { it.toString() })


    fun stepWithDesc(stepDesc: String, block: StepBlock<D, C>): DdtStep<D, C> =
        DdtStep(this, stepDesc, block)
}

private fun String.replaceDollars(parameters: List<String>): String = parameters
    .fold(this) { text, param ->
        text.replaceFirst("$", param)
    }
