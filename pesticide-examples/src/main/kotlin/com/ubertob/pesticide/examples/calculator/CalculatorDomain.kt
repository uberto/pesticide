package com.ubertob.pesticide.examples.calculator

import com.ubertob.pesticide.*
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.util.concurrent.atomic.AtomicInteger


fun allDomains() = setOf(
    InMemoryExampleDomain(),
    FakeHttpExampleDomain()
).asSequence()

interface ExampleDomainUnderTest :
    DomainUnderTest<DdtProtocol> {
    fun addNumber(num: Int)

    fun getTotal(): Int
    fun startWithNumber(num: Int): ExampleDomainUnderTest
}


class InMemoryExampleDomain :
    ExampleDomainUnderTest {

    var tot = 0

    override fun addNumber(num: Int) {
        tot += num
    }

    override fun getTotal(): Int = tot

    override fun startWithNumber(num: Int): ExampleDomainUnderTest {
        tot = num
        return this
    }

    override val protocol = InMemoryHubs

    override fun isStarted() = true

}


class FakeHttpExampleDomain :
    ExampleDomainUnderTest {

    val tot = AtomicInteger(0)

    override fun addNumber(num: Int) {
        tot.getAndUpdate { it + num }
    }

    override fun getTotal(): Int = tot.get()
    override fun startWithNumber(num: Int): ExampleDomainUnderTest {
        tot.set(num)
        return this
    }

    override val protocol = PureHttp("fake")

    override fun isStarted() = true

}

data class Scolar(override val name: String) : DdtActor<ExampleDomainUnderTest> {
    fun `tells a number`(num: Int) = executeStep("Scolar tell $num to system") { addNumber(num) }
    fun `verifies the total`(expected: Int) = executeStep("Scolar verify the total is $expected") {
        expectThat(getTotal()).isEqualTo(expected)
    }
}
