package com.ubertob.pesticide

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.opentest4j.TestAbortedException
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicReference
import kotlin.streams.asStream


data class Scenario<D : BoundedContextInterpreter<*>>(val steps: Iterable<DdtStep<D>>, val wipData: WipData? = null) :
        (D) -> DynamicContainer {

    var alreadyFailed = false

    override fun invoke(domain: D): DynamicContainer {
        assertEquals(Ready, domain.prepare(), "Protocol ${domain.protocol.desc} ready")

        val tests = trapUnexpectedExceptions {
            createTests(domain)
        }

        val inWip = getDueDate(wipData, domain)?.let { "WIP till $it - " } ?: ""

        return DynamicContainer.dynamicContainer("$inWip${domain.description()}", tests.asStream())
    }

    fun createTests(domain: D): Sequence<DynamicNode> {
        val currentDomain = AtomicReference(domain)

        return steps.map { step ->
            createTest(step, currentDomain)
        }.asSequence()
    }

    private fun createTest(
        step: DdtStep<D>,
        currentDomain: AtomicReference<D>
    ): DynamicTest = dynamicTest(decorateTestName(currentDomain.get(), step), step.testSourceURI()) {
        currentDomain.set(execute(currentDomain.get(), step))
    }

    private fun execute(
        domainUnderTest: D,
        step: DdtStep<D>
    ): D =
        checkWIP(wipData, domainUnderTest, {
            Assumptions.assumeFalse(alreadyFailed, "Skipped because of previous failures")

            try {
                step.action(domainUnderTest)
            } catch (t: Throwable) {
                alreadyFailed = true
                throw t
            }

        })


    private fun decorateTestName(domainUnderTest: D, step: DdtStep<D>) =
        "${domainUnderTest.protocol.desc} - ${step.description}"


    private fun checkWIP(wipData: WipData?, domain: D, testBlock: (D) -> D): D =
        getDueDate(wipData, domain)
            ?.let { executeInWIP(it, testBlock)(domain) }
            ?: testBlock(domain)


    private fun getDueDate(wipData: WipData?, domain: D): LocalDate? =
        if (wipData == null || wipData.shouldWorkFor(domain.protocol))
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

    fun BoundedContextInterpreter<*>.description(): String = "${javaClass.simpleName} - ${protocol.desc}"


    private fun <D : BoundedContextInterpreter<*>> executeInWIP(
        due: LocalDate,
        testBlock: (D) -> D
    ): (D) -> D = { domain ->
        if (due < LocalDate.now()) {
            fail("Due date expired $due")
        } else {
            try {
                testBlock(domain)
            } catch (aborted: TestAbortedExceptionWIP) {
                throw aborted //nothing to do here
            } catch (t: Throwable) {
                //all the rest
                throw TestAbortedExceptionWIP(
                    "Test failed but this is ok until $due",
                    t
                )
            }
            throw TestAbortedExceptionWIP("Test succeded but you have to remove the WIP marker!")
        }
    }

    data class TestAbortedExceptionWIP(override val message: String, val throwable: Throwable? = null) :
        TestAbortedException(message, throwable)

}

