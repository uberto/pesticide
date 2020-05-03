package com.ubertob.pesticide.examples.calculator

import com.ubertob.pesticide.*
import java.util.concurrent.atomic.AtomicInteger


fun allCalculatorAbstractions() = setOf(
    InMemoryCalculatorDomain(),
    FakeHttpCalculatorDomain()
)

interface CalculatorDomainWrapper : DomainUnderTest<DdtProtocol> {
    fun addNumber(num: Int)

    fun getTotal(): Int
    fun startWithNumber(num: Int): CalculatorDomainWrapper
}


class InMemoryCalculatorDomain :
    CalculatorDomainWrapper {

    var tot = 0

    override fun addNumber(num: Int) {
        tot += num
    }

    override fun getTotal(): Int = tot

    override fun startWithNumber(num: Int): CalculatorDomainWrapper {
        tot = num
        return this
    }

    override val protocol = InMemoryHubs

    override fun prepare(): DomainSetUp = Ready

}


class FakeHttpCalculatorDomain :
    CalculatorDomainWrapper {

    val tot = AtomicInteger(0)

    override fun addNumber(num: Int) {
        tot.getAndUpdate { it + num }
    }

    override fun getTotal(): Int = tot.get()
    override fun startWithNumber(num: Int): CalculatorDomainWrapper {
        tot.set(num)
        return this
    }

    override val protocol = PureHttp("fake")

    override fun prepare(): DomainSetUp = Ready

}

