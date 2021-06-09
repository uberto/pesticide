package com.ubertob.pesticide.core

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.opentest4j.AssertionFailedError
import org.opentest4j.TestAbortedException
import java.time.LocalDate
import kotlin.streams.asStream

/**
 * DdtScenario is the class that keeps together the information to create a scenario test. It can generate the {@code DynamicTest}
 *
 * Normally it shouldn't be created directly but using the {@code ddtScenario} method of {@code DomainDrivenTest}
 */
data class DdtScenario<D : DdtActions<*>>(
    val ddtSetup: DdtSetup<D>,
    val steps: List<DdtStep<D, *>>,
    val wipData: WipData? = null
) : (D) -> DynamicContainer {

    val ALREADY_FAILED = "_alreadyFailed"
    val contextStore = ContextStore()

    override fun invoke(actions: D): DynamicContainer {
        assertEquals(
            Ready,
            actions.prepare(),
            "Actions for protocol ${actions.protocol.desc} failed to start properly"
        )

        val tests = trapUnexpectedExceptions {
            createTests(actions)
        }

        val inWipPrefix = getDueDate(wipData, actions.protocol)?.let { "WIP - " } ?: ""

        return DynamicContainer.dynamicContainer(
            "$inWipPrefix${actions.description()}",
            tests.asSequence().asStream()
        )
    }


    private fun createTests(actions: D): List<DynamicNode> =
        createTest(ddtSetup.asStep(), actions)
        {
            contextStore.clear()
            contextStore.store(ALREADY_FAILED, false)
            decorateExecution(
                actions,
                ddtSetup.asStep(),
                StepContext(ddtSetup.asStep().actorName, contextStore)
            )
        } prependTo steps.map { step ->
            createTest(step, actions) {
                decorateExecution(actions, step, StepContext(step.actorName, contextStore))
            }
        }.addFinalWipTestIfNeeded(wipData, actions.protocol)


    private fun <C : Any> createTest(
        step: DdtStep<D, C>,
        actions: D,
        executable: () -> Unit
    ): DynamicTest = dynamicTest(decorateTestName(actions, step), step.testSourceURI(), executable)

    private fun <C : Any> decorateExecution(
        actions: D,
        step: DdtStep<D, C>,
        stepContext: StepContext<C>
    ) {
        ignoreIfAlreadyFailed(wipData, actions.protocol) {
            Assumptions.assumeFalse(
                alreadyFailed(), "Skipped because of previous failures"
            )
            Thread.currentThread().name = "DDT-${step.description}"
            try {
                step.action(actions, stepContext)
            } catch (t: Throwable) {
                contextStore.store(ALREADY_FAILED, true)
                throw decorateException(step, actions, t)
            }

        }
    }

    private fun <C : Any> decorateException(
        step: DdtStep<D, C>,
        actions: D,
        t: Throwable
    ): Throwable =
        when (t) {
            is AssertionFailedError -> DDTAssertionFailedError(
                t,
                step.testSourceString(),
                decorateTestName(actions, step)
            )
            is AssertionError -> DDTAssertionError(t, step.testSourceString(), decorateTestName(actions, step))
            else -> DDTError(t, step.testSourceString(), decorateTestName(actions, step))
        }


    data class DDTError(val t: Throwable, val sourceLine: String, val testName: String) : Throwable()
    data class DDTAssertionError(val t: AssertionError, val sourceLine: String, val testName: String) :
        AssertionError(t.message)

    data class DDTAssertionFailedError(val t: AssertionFailedError, val sourceLine: String, val testName: String) :
        AssertionFailedError(t.message, t.expected, t.actual, t.cause)

    private fun alreadyFailed() = contextStore.get(ALREADY_FAILED) as Boolean


    private fun decorateTestName(actions: D, step: DdtStep<D, *>) =
        "${actions.protocol.desc} - ${step.description}"


    private fun ignoreIfAlreadyFailed(wipData: WipData?, protocol: DdtProtocol, testBlock: () -> Unit) =
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

    fun DdtActions<*>.description(): String = javaClass.simpleName


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






