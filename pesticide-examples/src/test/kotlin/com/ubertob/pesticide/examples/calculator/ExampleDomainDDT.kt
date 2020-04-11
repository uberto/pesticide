package com.ubertob.pesticide.examples.simpleexample.com.ubertob.pesticide.simpleexample

import com.ubertob.pesticide.DDT
import com.ubertob.pesticide.DomainDrivenTest
import com.ubertob.pesticide.examples.calculator.ExampleDomainUnderTest
import com.ubertob.pesticide.examples.calculator.Scolar
import com.ubertob.pesticide.examples.calculator.allDomains
import java.time.LocalDate
import kotlin.random.Random


class ExampleDomainDDT : DomainDrivenTest<ExampleDomainUnderTest>(
    allDomains()
) {

    val bart = Scolar()

    @DDT
    internal fun `style GWT`() = ddtScenario {
        onStage {
            startWithNumber(128)
        } actorsPlay steps(
            bart.`tells a number`(64),
            bart.`tells a number`(31),
            bart.`tells a number`(33),
            bart.`verifies the total`(256)
        )
    }


    @DDT
    internal fun `new style with fixture`() = ddtScenario {
        val n1 = 64
        val n2 = 31
        val n3 = 33

        onStage {
            startWithNumber(128)
        } actorsPlay steps(
            bart.`tells a number`(n1),
            bart.`tells a number`(n2),
            bart.`tells a number`(n3),
            bart.`verifies the total`(256)
        )
    }

    @DDT
    internal fun `new style with fixture and WIP`() = ddtScenario {
        val rndNum = Random.nextInt()

        onStage {
            startWithNumber(100)
        } actorsPlay steps(
            bart.`tells a number`(rndNum),
            bart.`verifies the total`(99)
        ).wip(LocalDate.of(2100, 1, 1))
    }


    @DDT
    internal fun `new style without set up`() = ddtScenario {
        val rndNum = Random.nextInt()

        onEmptyStage actorsPlay steps(
            bart.`tells a number`(rndNum),
            bart.`verifies the total`(99)
        ).wip(LocalDate.of(2100, 1, 1))
    }

}

