package com.ubertob.pesticide.teststeps

import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class SkipAfterFailTest {


    @TestFactory
    fun `multiple steps test`(): Collection<DynamicTest> =
        (0..100).map {
            dynamicTest(
                "Test $it"
            ) {
                Assumptions.assumeTrue(it % 3 == 0)

                expectThat(it + it).isEqualTo(2 * it)
            }
        }

}