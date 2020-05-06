package com.ubertob.pesticide

@Deprecated("Use BoundedContextInterpreter instead")
typealias DomainUnderTest<P> = BoundedContextInterpreter<P>

interface BoundedContextInterpreter<out P : DdtProtocol> {
    val protocol: P

    fun prepare(): DomainSetUp

}


sealed class DomainSetUp
object Ready : DomainSetUp()
data class NotReady(val reason: String) : DomainSetUp()