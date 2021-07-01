package com.ubertob.pesticide.examples.calculator

import com.ubertob.pesticide.core.DDT
import com.ubertob.pesticide.core.DomainDrivenTest
import com.ubertob.pesticide.core.DomainOnly
import java.time.LocalDate
import kotlin.random.Random


class CalculatorDDT : DomainDrivenTest<CalculatorInterpreter>(allCalculatorInterpreters()) {

    val bart by NamedUser(::Student)

    @DDT
    fun `sum three numbers`() = useCase {

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
    fun `sum three numbers NG`() = useCase {

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
    fun `with fixture and Work In Progress`() = useCase {
        val rndNum = Random.nextInt(1, 100)

        setUp {
            startWithNumber(100)
        }.thenPlay(
            bart.`adds number #`(rndNum),
            bart.`verifies the total is #`(100)
        ).wip(LocalDate.of(2100, 1, 1), "Waiting for new century")

    }

    @DDT
    fun `with fixture`() = useCase {
        val n1 = 64
        val n2 = 31
        val n3 = 33

        setUp {
            startWithNumber(128)
        }.thenPlay(
            bart.`adds number #`(n1),
            bart.`adds number #`(n2),
            bart.`adds number #`(n3),

            bart.`verifies the total is #`(256)
        )
    }


    @DDT
    fun `without setting`() = useCase {
        val rndNum = Random.nextInt()

        play(
            bart.`adds number #`(rndNum),
            bart.`verifies the total is #`(rndNum)
        )
    }


    @DDT
    fun `without setting NG`() = useCase {
        val rndNum = Random.nextInt()

        play(
            bart.`adds number #`(rndNum),
            bart.`verifies the total is #`(rndNum)
        )
    }

    @DDT
    fun `with Work In Progress and exceptions`() = useCase {
        val rndNum = Random.nextInt(1, 100)

        setUp {
            startWithNumber(200)
        }.thenPlay(
            bart.`subtracts number #`(rndNum),
            bart.`verifies the total greater than #`(100)
        ).wip(LocalDate.of(2100, 1, 1), "Working only in DomainOnly", setOf(DomainOnly::class))

    }


    @DDT
    fun `first failed test stops the others`() = useCase {
        val rndNum = Random.nextInt(1, 100)

        setUp {
            startWithNumber(100)
        }.thenPlay(
            bart.`subtracts number #`(rndNum),
            bart.`verifies the total greater than #`(100),
            bart.`adds number #`(rndNum),
            bart.`verifies the total is #`(100)
        ).wip(LocalDate.of(2100, 1, 1), "wrong test")

    }

}


