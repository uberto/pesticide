package com.ubertob.pesticide.core


/**
 * DomainInterpreter is the interface to inherit from to define the api to drive the domain.
 * The domain specific interface must be implemented for each of the protocols.
 *
 * DomainInterpreters should work as a Facade to abstract the specific protocol details,
 * I should not contain domain related assertions, those should be used in the actors only.
 * It can contain assertions related to technical layers of the specific protocols.
 *
 * see also {@link DdtProtocol} and {@link DdtActorWithContext}
 *
 */


interface DomainInterpreter<out P : DdtProtocol> {

    val protocol: P

    fun prepare(): DomainSetUp

}


sealed class DomainSetUp
object Ready : DomainSetUp()
data class NotReady(val reason: String) : DomainSetUp()