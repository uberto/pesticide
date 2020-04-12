package com.ubertob.pesticide.examples.simpleexample.com.ubertob.pesticide.simpleexample

import com.ubertob.pesticide.ActorDelegate
import com.ubertob.pesticide.DDT
import com.ubertob.pesticide.DomainDrivenTest
import com.ubertob.pesticide.examples.calculator.CalculatorDomain
import com.ubertob.pesticide.examples.calculator.Student
import com.ubertob.pesticide.examples.calculator.allProtocols
import java.time.LocalDate
import kotlin.random.Random


class CalculatorDomainDDT : DomainDrivenTest<CalculatorDomain>(allProtocols()) {

    val bart by ActorDelegate(::Student)

    @DDT
    internal fun `sum three numbers`() = ddtScenario {
        setting {
            startWithNumber(128)
        } atRise steps(
            bart.`tells a number`(64),
            bart.`tells a number`(31),
            bart.`tells a number`(33),
            bart.`verifies the total`(256)
        )
    }


    @DDT
    internal fun `with fixture`() = ddtScenario {
        val n1 = 64
        val n2 = 31
        val n3 = 33

        setting {
            startWithNumber(128)
        } atRise steps(
            bart.`tells a number`(n1),
            bart.`tells a number`(n2),
            bart.`tells a number`(n3),
            bart.`verifies the total`(256)
        )
    }

    @DDT
    internal fun `with fixture and Work In Progress`() = ddtScenario {
        val rndNum = Random.nextInt()

        setting {
            startWithNumber(100)
        } atRise steps(
            bart.`tells a number`(rndNum),
            bart.`verifies the total`(99)
        ).wip(LocalDate.of(2100, 1, 1))
    }


    @DDT
    internal fun `without setting`() = ddtScenario {
        val rndNum = Random.nextInt()

        withoutSetting atRise steps(
            bart.`tells a number`(rndNum),
            bart.`verifies the total`(rndNum)
        )
    }

}


