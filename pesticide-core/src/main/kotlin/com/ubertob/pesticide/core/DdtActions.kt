package com.ubertob.pesticide.core


/**
 * DdtActions is the interface that contains all the possible actions on the domain.
 * The domain specific interface must be implemented for each of the protocols.
 *
 * DdtActions should work as a Facade to abstract the specific protocol details,
 * It should not contain domain related assertions, those should be used only inside the Steps.
 * It can contain assertions related to technical layers of the specific protocols.
 *
 * see also {@link DdtProtocol} and {@link DdtActorWithContext}
 *
 */

typealias DomainInterpreter<P> = DdtActions<P>


interface DdtActions<out P : DdtProtocol> {

    val protocol: P

    fun prepare(): DomainSetUp

}


sealed class DomainSetUp
object Ready : DomainSetUp()
data class NotReady(val reason: String) : DomainSetUp()