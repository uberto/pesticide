package com.ubertob.pesticide.examples.calculator

import com.ubertob.pesticide.core.DdtActor
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan

data class Student(override val name: String) : DdtActor<CalculatorInterpreter>() {

    fun `adds number #`(num: Int) = step(num) { addNumber(num) }

    fun `verifies the total is #`(expected: Int) = step(expected) {
        expectThat(getTotal()).isEqualTo(expected)
    }

    fun `verifies the total greater than #`(expected: Int) = step(expected) {
        expectThat(getTotal()).isGreaterThan(expected)
    }

    fun `subtracts number #`(num: Int) = step(num) { subtractNumber(num) }
}