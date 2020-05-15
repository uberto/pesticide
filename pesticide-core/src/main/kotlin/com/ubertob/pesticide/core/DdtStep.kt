package com.ubertob.pesticide.core

import java.io.File
import java.net.URI

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
