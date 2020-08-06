package com.ubertob.pesticide.core

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.opentest4j.TestAbortedException
import java.time.LocalDate
import kotlin.streams.asStream

/**
 * DdtScenario is the class that keeps together the information to create a scenario test. It can generate the {@code DynamicTest}
 *
 * Normally it shouldn't be created directly but using the {@code ddtScenario} method of {@code DomainDrivenTest}
 */
data class DdtScenario<D : DomainInterpreter<*>>(
    val setting: Setting<D>,
    val steps: List<DdtStep<D, *>>,
    val wipData: WipData? = null
) : (D) -> DynamicContainer {

    val ALREADY_FAILED = "_alreadyFailed"
    val contextStore = ContextStore()

    override fun invoke(domainInterpreter: D): DynamicContainer {
        assertEquals(
            Ready,
            domainInterpreter.prepare(),
            "Interpreter ${domainInterpreter.protocol.desc} failed to start properly"
        )

        val tests = trapUnexpectedExceptions {
            createTests(domainInterpreter)
        }

        val inWipPrefix = getDueDate(wipData, domainInterpreter.protocol)?.let { "WIP - " } ?: ""

        return DynamicContainer.dynamicContainer(
            "$inWipPrefix${domainInterpreter.description()}",
            tests.asSequence().asStream()
        )
    }


    private fun createTests(domainInterpreter: D): List<DynamicNode> =
        createTest(setting.asStep(), domainInterpreter)
        {
            contextStore.clear()
            contextStore.store(ALREADY_FAILED, false)
            decorateExecution(
                domainInterpreter,
                setting.asStep(),
                StepContext(setting.asStep().actor.name, contextStore)
            )
        } prependTo steps.map { step ->
            createTest(step, domainInterpreter) {
                decorateExecution(domainInterpreter, step, StepContext(step.actor.name, contextStore))
            }
        }.addFinalWipTestIfNeeded(wipData, domainInterpreter.protocol)


    private fun <C : Any> createTest(
        step: DdtStep<D, C>,
        domainInterpreter: D,
        executable: () -> Unit
    ): DynamicTest = dynamicTest(decorateTestName(domainInterpreter, step), step.testSourceURI(), executable)

    private fun <C : Any> decorateExecution(
        interpreter: D,
        step: DdtStep<D, C>,
        stepContext: StepContext<C>
    ) {
        checkWIP(wipData, interpreter.protocol) {
            Assumptions.assumeFalse(
                alreadyFailed(), "Skipped because of previous failures"
            )

            try {
                step.action(interpreter, stepContext)
            } catch (t: Throwable) {
                contextStore.store(ALREADY_FAILED, true)
                throw t
            }

        }
    }

    private fun alreadyFailed() = contextStore.get(ALREADY_FAILED) as Boolean


    private fun decorateTestName(domainInterpreter: D, step: DdtStep<D, *>) =
        "${domainInterpreter.protocol.desc} - ${step.description}"


    private fun checkWIP(wipData: WipData?, protocol: DdtProtocol, testBlock: () -> Unit) =
        getDueDate(wipData, protocol)
            ?.let { executeInWIP(it, testBlock) }
            ?: testBlock()


    private fun getDueDate(wipData: WipData?, protocol: DdtProtocol): LocalDate? =
        if (wipData == null || wipData.shouldWorkFor(protocol))
            null
        else
            wipData.dueDate

    private fun <T : Any> trapUnexpectedExceptions(block: () -> T): T =
        try {
            block()
        } catch (t: Throwable) {
            fail(
                "Unexpected Exception while creating the tests. Have you forgotten to use generateStep in your actors? ",
                t
            )
        }

    fun DomainInterpreter<*>.description(): String = javaClass.simpleName


    private fun executeInWIP(
        due: LocalDate,
        testBlock: () -> Unit
    ) {
        if (due < LocalDate.now()) {
            fail("Due date expired $due")
        } else {
            try {
                testBlock()
            } catch (aborted: TestAbortedExceptionWIP) {
                throw aborted //nothing to do here
            } catch (t: Throwable) {
                //all the rest
                throw TestAbortedExceptionWIP(
                    "Test failed but this is OK until $due",
                    t
                )
            }

        }
    }

    private fun List<DynamicTest>.addFinalWipTestIfNeeded(wipData: WipData?, protocol: DdtProtocol): List<DynamicTest> =
        if (wipData == null || wipData.shouldWorkFor(protocol))
            this
        else {
            this + dynamicTest("WIP till ${wipData.dueDate} because <${wipData.reason}>") {
                if (alreadyFailed())
                    throw TestAbortedExceptionWIP("Test marked as WIP failed. Ignoring it until ${wipData.dueDate}")
                else
                    fail("Test succeeded despite being marked as WIP. Please remove the WIP marker!")
            }
        }

    data class TestAbortedExceptionWIP(override val message: String, val throwable: Throwable? = null) :
        TestAbortedException(message, throwable)

    infix fun <T : Any> T.prependTo(list: List<T>) = listOf(this) + list

}






