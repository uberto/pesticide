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
    val userName: String,
    val description: String,
    val action: StepBlock<D, C>
) {

    val stepSourceLine = run {
        val stackTrace = Thread.currentThread().stackTrace
        stackTrace[5]
    }

    fun testSourceURI(): URI? =
        try {
            stepSourceLine?.toSourceReference()
        } catch (t: Throwable) {
            println("Error while trying to get the source line: $t")
            null
        }

    fun testSourceString(): String =
        try {
            stepSourceLine?.toSource().orEmpty()
        } catch (t: Throwable) {
            "Error while trying to get the source line: $t"
        }

    fun StackTraceElement.toSource(): String = "${testClass(className)}:$lineNumber"

    private val sourceRoot = listOf(
        File("src/test/kotlin"),
        File("src/test/java")
    ).find { it.isDirectory } ?: File(".")  //TODO make sure it works for others as well


    fun StackTraceElement.toSourceReference(): URI? {

        val type = testClass(className)
        val fileName = fileName ?: return null

        val uri = sourceRoot
            .toPath()
            .resolve(type.`package`.name.replace(".", "/"))
            .resolve(fileName)
            .toFile().toURI()
            .let { fileUri ->
                URI(
                    fileUri.scheme,
                    fileUri.userInfo,
                    fileUri.host,
                    fileUri.port,
                    "//" + fileUri.path,
                    "line=$lineNumber",
                    fileUri.fragment
                )
            }
        return uri

    }

    fun testClass(className: String): Class<*> {
        val dollar = className.indexOf('$')
        return if (dollar > 0)
            Class.forName(className.substring(0, dollar))
        else Class.forName(className)
    }

}
