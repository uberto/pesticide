package com.ubertob.pesticide.examples.junit5

import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class DynamicTest {


    @TestFactory
    fun `multiple steps test with skip`(): Collection<DynamicTest> =
        (0..100).map {
            dynamicTest(
                "Test $it"
            ) {
                Assumptions.assumeTrue(it % 3 == 0)

                expectThat(it + it).isEqualTo(2 * it)
            }
        }


    @TestFactory
    fun `multiple steps test failing`(): Collection<DynamicTest> =
        (0..10).map {
            dynamicTest(
                "Test $it"
            ) {
                expectThat(it % 2).isEqualTo(0)
            }
        }

}