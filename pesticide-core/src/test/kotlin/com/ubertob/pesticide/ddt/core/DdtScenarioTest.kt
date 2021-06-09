package com.ubertob.pesticide.ddt.core

import com.ubertob.pesticide.core.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicContainer
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError


class DdtScenarioTest {

    interface MiniInterpreter : DomainInterpreter<DdtProtocol> {
        fun ask(): Boolean
    }

    object MiniInterpreterFalse : MiniInterpreter {
        override val protocol = DomainOnly
        override fun prepare() = Ready

        override fun ask(): Boolean = false
    }

    object MiniInterpreterTrue : MiniInterpreter {
        override val protocol = DomainOnly
        override fun prepare() = Ready

        override fun ask(): Boolean = true
    }

    data class TestActor(override val name: String) : DdtActor<MiniInterpreter>()

    var step1run = false
    var step2run = false

    val actor = TestActor("frank")
    val step1 = DdtStep<MiniInterpreter, Unit>(actor.name, "step1") {
        step1run = true
        assertTrue(ask())
    }
    val step2 = DdtStep<MiniInterpreter, Unit>(actor.name, "step2") {
        step2run = true
    }
    val scenario = DdtScenario(DdtSetup(), listOf(step1, step2))

    @Test
    fun `scenario skip tests after the first failure`() {

        runScenario(scenario(MiniInterpreterFalse))

        assertTrue(step1run)
        assertFalse(step2run)

    }

    @Test
    fun `new protocol will run tests even if the previous one failed`() {
        runScenario(scenario(MiniInterpreterFalse))
        assertTrue(step1run)
        assertFalse(step2run)

        runScenario(scenario(MiniInterpreterTrue))
        assertTrue(step1run)
        assertTrue(step2run)
    }

    private fun runScenario(scenario: DynamicContainer) {
        try {
            scenario.children.forEach { (it as DynamicTest).executable.execute() }
        } catch (e: AssertionFailedError) {

        }
    }


}