package com.ubertob.pesticide.core

import java.net.URI


/**
 * DdtStep is the class that should drive the {@link DdtActions} and keep the Domain level assertions for all the {@link DdtProtocols}
 *
 * see {@link DdtUserWithContext} and {@link StepContext}
 *
 */
data class DdtStep<in D : DdtActions<*>, C : Any>(
    val userName: String,
    val description: String,
    val action: StepBlock<D, C>
) {

    val stackTraceElement = run {
        Thread.currentThread().stackTrace[5]//TODO can we guess better the DDT file from the stacktrace?
    }

    fun testSourceURI(): URI? =
        try {
            stackTraceElement?.toSourceReference()
        } catch (t: Throwable) {
            println("Error while trying to get the source line: $t")
            null
        }

    fun testSourceString(): String =
        try {
            stackTraceElement?.toSource().orEmpty()
        } catch (t: Throwable) {
            "Error while trying to get the source line: $t"
        }

    fun StackTraceElement.toSource(): String = "${testClass(className)}:$lineNumber"

    fun StackTraceElement.toSourceReference(): URI? {

        val fileName = fileName ?: return null
        val type = testClass(className)

        val pathpesticide = "classpath:/${type.`package`.name.replace(".", "/")}/$fileName"

        return URI("$pathpesticide&line=$lineNumber").also { println(it) }

    }

    fun testClass(className: String): Class<*> {
        val dollar = className.indexOf('$')
        return if (dollar > 0)
            Class.forName(className.substring(0, dollar))
        else Class.forName(className)
    }

}
