package com.ubertob.pesticide.examples.calculator

import com.ubertob.pesticide.DdtActor
import strikt.api.expectThat
import strikt.assertions.isEqualTo

data class Student(override val name: String) : DdtActor<CalculatorDomainWrapper>() {
    fun `add number $`(num: Int) = step(num) { addNumber(num) }
    fun `verifies the total is $`(expected: Int) = step(expected) {
        expectThat(getTotal()).isEqualTo(expected)
    }
}