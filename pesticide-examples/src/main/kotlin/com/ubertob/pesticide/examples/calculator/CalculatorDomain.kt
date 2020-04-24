package com.ubertob.pesticide.examples.calculator

import com.ubertob.pesticide.*
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.util.concurrent.atomic.AtomicInteger


fun allCalculatorAbstractions() = setOf(
    InMemoryCalculatorDomain(),
    FakeHttpCalculatorDomain()
)

interface CalculatorDomain :
    DomainUnderTest<DdtProtocol> {
    fun addNumber(num: Int)

    fun getTotal(): Int
    fun startWithNumber(num: Int): CalculatorDomain
}


class InMemoryCalculatorDomain :
    CalculatorDomain {

    var tot = 0

    override fun addNumber(num: Int) {
        tot += num
    }

    override fun getTotal(): Int = tot

    override fun startWithNumber(num: Int): CalculatorDomain {
        tot = num
        return this
    }

    override val protocol = InMemoryHubs

    override fun isReady() = true

}


class FakeHttpCalculatorDomain :
    CalculatorDomain {

    val tot = AtomicInteger(0)

    override fun addNumber(num: Int) {
        tot.getAndUpdate { it + num }
    }

    override fun getTotal(): Int = tot.get()
    override fun startWithNumber(num: Int): CalculatorDomain {
        tot.set(num)
        return this
    }

    override val protocol = PureHttp("fake")

    override fun isReady() = true

}

data class Student(override val name: String) : DdtActor<CalculatorDomain>() {
    fun `tells a number`(num: Int) = generateStep("Scolar tell $num to system") { addNumber(num) }
    fun `verifies the total`(expected: Int) = generateStep("Scolar verify the total is $expected") {
        expectThat(getTotal()).isEqualTo(expected)
    }
}
