package com.ubertob.pesticide.examples.calculator

import com.ubertob.pesticide.core.DDT
import com.ubertob.pesticide.core.DomainDrivenTest
import com.ubertob.pesticide.core.DomainOnly
import java.time.LocalDate
import kotlin.random.Random


class CalculatorDDT : DomainDrivenTest<CalculatorInterpreter>(allCalculatorInterpreters()) {

    val bart by NamedActor(::Student)

    @DDT
    fun `sum three numbers`() = ddtScenario {

        setting {
            startWithNumber(128)
        } atRise play(
            bart.`adds number #`(64),
            bart.`adds number #`(31),
            bart.`adds number #`(33),
            bart.`verifies the total is #`(256)
        )
    }

    @DDT
    fun `sum three numbers NG`() = ddtScenario {

        setUp {
            startWithNumber(128)
        }.thenPlay(
            bart.`adds number #`(64),
            bart.`adds number #`(31),
            bart.`adds number #`(33),
            bart.`verifies the total is #`(256)
        )
    }


    @DDT
    fun `with fixture and Work In Progress`() = ddtScenario {
        val rndNum = Random.nextInt(1, 100)

        setting {
            startWithNumber(100)
        } atRise play(
            bart.`adds number #`(rndNum),
            bart.`verifies the total is #`(100)
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
            bart.`adds number #`(n1),
            bart.`adds number #`(n2),
            bart.`adds number #`(n3),

            bart.`verifies the total is #`(256)
        )
    }


    @DDT
    fun `without setting`() = ddtScenario {
        val rndNum = Random.nextInt()

        withoutSetting atRise play(
            bart.`adds number #`(rndNum),
            bart.`verifies the total is #`(rndNum)
        )
    }


    @DDT
    fun `without setting NG`() = ddtScenario {
        val rndNum = Random.nextInt()

        play(
            bart.`adds number #`(rndNum),
            bart.`verifies the total is #`(rndNum)
        )
    }

    @DDT
    fun `with Work In Progress and exceptions`() = ddtScenario {
        val rndNum = Random.nextInt(1, 100)

        setting {
            startWithNumber(200)
        } atRise play(
            bart.`subtracts number #`(rndNum),
            bart.`verifies the total greater than #`(100)
        ).wip(LocalDate.of(2100, 1, 1), "Working only in DomainOnly", setOf(DomainOnly::class))

    }


    @DDT
    fun `first failed test stops the others`() = ddtScenario {
        val rndNum = Random.nextInt(1, 100)

        setting {
            startWithNumber(100)
        } atRise play(
            bart.`subtracts number #`(rndNum),
            bart.`verifies the total greater than #`(100),
            bart.`adds number #`(rndNum),
            bart.`verifies the total is #`(100)
        ).wip(LocalDate.of(2100, 1, 1), "wrong test")

    }

}


