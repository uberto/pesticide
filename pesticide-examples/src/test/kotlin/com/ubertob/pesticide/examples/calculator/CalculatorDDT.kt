package com.ubertob.pesticide.examples.calculator

import com.ubertob.pesticide.core.DDT
import com.ubertob.pesticide.core.DomainDrivenTest
import java.time.LocalDate
import kotlin.random.Random


class CalculatorDDT : DomainDrivenTest<CalculatorInterpreter>(allCalculatorInterpreters()) {

    val bart by NamedActor(::Student)

    @DDT
    fun `sum three numbers`() = ddtScenario {

        setting {
            startWithNumber(128)
        } atRise play(
            bart.`add number $`(64),
            bart.`add number $`(31),
            bart.`add number $`(33),
            bart.`verifies the total is $`(256)
        )
    }

    @DDT
    fun `with fixture and Work In Progress`() = ddtScenario {
        val rndNum = Random.nextInt(1, 100)

        setting {
            startWithNumber(100)
        } atRise play(
            bart.`add number $`(rndNum),
            bart.`verifies the total is $`(100)
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
            bart.`add number $`(n1),
            bart.`add number $`(n2),
            bart.`add number $`(n3),

            bart.`verifies the total is $`(256)
        )
    }


    @DDT
    fun `without setting`() = ddtScenario {
        val rndNum = Random.nextInt()

        withoutSetting atRise play(
            bart.`add number $`(rndNum),
            bart.`verifies the total is $`(rndNum)
        )
    }

}


