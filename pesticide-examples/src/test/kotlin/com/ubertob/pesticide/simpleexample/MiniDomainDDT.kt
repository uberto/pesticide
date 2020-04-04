package com.ubertob.pesticide.simpleexample

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

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
}