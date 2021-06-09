package com.ubertob.pesticide.core

import java.io.File
import java.net.URI


/**
 * DdtStep is the class that should drive the {@link DdtActions} and keep the Domain level assertions for all the {@link DdtProtocols}
 *
 * see {@link DdtActorWithContext} and {@link StepContext}
 *
 */
data class DdtStep<in D : DdtActions<*>, C : Any>(
    val actorName: String,
    val description: String,
    val action: StepBlock<D, C>
) {
    val stackTraceElement = Thread.currentThread().stackTrace[5]//TODO can we guess better the sourceRoot?

    fun testSourceURI(): URI? =
        try {
            stackTraceElement?.toSourceReference(sourceRoot)
        } catch (t: Throwable) {
            println("Error while trying to get the source line: $t")
            null
        }

    fun StackTraceElement.toSourceReference(sourceRoot: File): URI? {

        val fileName = fileName ?: return null
        val type = Class.forName(className)

        val pathpesticide =
            sourceRoot.toPath().resolve(type.`package`.name.replace(".", "/")).resolve(fileName).toFile().toURI()

        return URI("$pathpesticide&line=$lineNumber")

    }

}

private val sourceRoot = listOf(
    File("src/test/kotlin"),
    File("src/test/java")
).find { it.isDirectory } ?: File(".")  //TODO make sure it works for others as well
