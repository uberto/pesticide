package com.ubertob.pesticide.ddt.core

import com.ubertob.pesticide.core.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicContainer
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError


class DdtScenarioTest {

    interface MiniActions : DomainActions<DdtProtocol> {
        fun ask(): Boolean
    }

    object MiniActionsFalse : MiniActions {
        override val protocol = DomainOnly
        override fun prepare() = Ready

        override fun ask(): Boolean = false
    }

    object MiniActionsTrue : MiniActions {
        override val protocol = DomainOnly
        override fun prepare() = Ready

        override fun ask(): Boolean = true
    }

    data class TestUser(override val name: String) : DdtActor<MiniActions>()

    var step1run = false
    var step2run = false

    val user = TestUser("frank")
    val step1 = DdtStep<MiniActions, Unit>(user.name, "step1") {
        step1run = true
        assertTrue(ask())
    }
    val step2 = DdtStep<MiniActions, Unit>(user.name, "step2") {
        step2run = true
    }
    val useCase = DdtScenario(DdtSetup(), listOf(step1, step2))

    @Test
    fun `DDT skips all steps after the first failure`() {

        runScenario(useCase(MiniActionsFalse))

        assertTrue(step1run)
        assertFalse(step2run)

    }

    @Test
    fun `new protocol will run tests even if the previous one failed`() {
        runScenario(useCase(MiniActionsFalse))
        assertTrue(step1run)
        assertFalse(step2run)

        runScenario(useCase(MiniActionsTrue))
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