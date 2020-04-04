package com.ubertob.pesticide.simpleexample

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.reflect.KProperty

class MiniDomainDDT {


// put all the conditions in the Given part
//

    @Test
    fun `do something`() {
        val fixture = object {
            val name = "Fred"
            val age = 45
            val married = false
        }

        verify(fixture) {
            expectThat(name).isEqualTo("Fred")
        }

    }


    fun <T> verify(f: T, block: T.(String) -> Unit) = block(f, "kiss")


    // construct actors
    val ann by ActorRef(::User)


    @Test
    fun `ann does something`() {
        expectThat(ann.name).isEqualTo("Ann")

    }

}

class ActorRef(val actorConstructor: (String) -> Actor) {
    operator fun getValue(miniDomainDDT: MiniDomainDDT, property: KProperty<*>): Actor =
        actorConstructor(property.name)


}

interface Actor {
    val name: String
}


data class User(override val name: String) : Actor
