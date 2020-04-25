package com.ubertob.pesticide.examples.calculator

import com.ubertob.pesticide.DDT
import com.ubertob.pesticide.DomainDrivenTest
import com.ubertob.pesticide.NamedActor
import java.time.LocalDate
import kotlin.random.Random


class CalculatorDDT : DomainDrivenTest<CalculatorDomain>(allCalculatorAbstractions()) {

    val bart by NamedActor(::Student)

    @DDT
    fun `sum three numbers`() = ddtScenario {

        setting {
            startWithNumber(128)
        } atRise play(
            bart.`tells a number`(64),
            bart.`tells a number`(31),
            bart.`tells a number`(33),
            bart.`verifies the total`(256)
        )
    }

    @DDT
    fun `with fixture and Work In Progress`() = ddtScenario {
        val rndNum = Random.nextInt(1, 100)

        setting {
            startWithNumber(100)
        } atRise play(
            bart.`tells a number`(rndNum),
            bart.`verifies the total`(99)
        ).wip(LocalDate.of(2100, 1, 1), "Waiting for new century")

    }

    @DDT
    fun `with fixture`() = ddtScenario {
        val n1 = 64
        val n2 = 31
        val n3 = 33

        setting {
            startWithNumber(128)
        } atRise play(
            bart.`tells a number`(n1),
            bart.`tells a number`(n2),
            bart.`tells a number`(n3),

            bart.`verifies the total`(256)
        )
    }


    @DDT
    fun `without setting`() = ddtScenario {
        val rndNum = Random.nextInt()

        withoutSetting atRise play(
            bart.`tells a number`(rndNum),
            bart.`verifies the total`(rndNum)
        )
    }

}


