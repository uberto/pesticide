package com.ubertob.pesticide.examples.junit5

import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.DynamicContainer.dynamicContainer
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.util.stream.Stream

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

//
//    @TestFactory
//    fun `multiple steps test failing`(): Collection<DynamicTest> =
//        (0..10).map {
//            dynamicTest(
//                "Test $it"
//            ) {
//                expectThat(it % 2).isEqualTo(0)
//            }
//        }


    @TestFactory
    fun `dynamic nodes`(): Stream<out DynamicNode> =
        (0..5).map {
            dynamicContainer(
                "container $it",
                (1..10).map {

                    dynamicTest(
                        "Test $it"
                    ) {
                        trivialExpectation(it)
                    }
                }.stream()
            )
        }.stream()

    private fun trivialExpectation(num: Int) {
        expectThat(num + num).isEqualTo(2 * num)

//        if (num % 7 == 0)
//            fail("7777")
    }


}