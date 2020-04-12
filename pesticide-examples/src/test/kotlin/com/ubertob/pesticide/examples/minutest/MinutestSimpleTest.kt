package com.ubertob.pesticide.examples.minutest

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class MyFirstMinutests : JUnit5Minutests {

    fun tests() = rootContext<Unit> {

        test("addition") {
            expectThat(1 + 1).isEqualTo(2)
        }


        test("multiplication") {
            expectThat(2 * 2).isEqualTo(4)
        }

//
//        test("failing") {
//            expectThat(2).isEqualTo(4)
//        }


    }
}
