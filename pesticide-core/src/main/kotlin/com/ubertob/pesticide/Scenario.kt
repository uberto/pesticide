package com.ubertob.pesticide

import org.junit.jupiter.api.*
import org.junit.jupiter.api.DynamicTest.dynamicTest
import java.time.LocalDate
import kotlin.streams.asStream


data class Scenario<D : DomainUnderTest<*>>(val steps: Iterable<DdtStep<D>>, val wipData: WipData? = null) :
        (D) -> DynamicContainer {

    var alreadyFailed = false //TODO replace it with a proper fold

    override fun invoke(domain: D): DynamicContainer {
        Assertions.assertTrue(domain.isReady(), "Protocol ${domain.protocol.desc} ready")

        val tests = trapUnexpectedExceptions {
            createTests(domain)
        }

        val inWip = getDueDate(wipData, domain)?.let { "WIP till $it - " } ?: ""

        return DynamicContainer.dynamicContainer("$inWip${domain.description()}", tests.asStream())
    }


    fun createTests(domain: D): Sequence<DynamicNode> =
        steps.asSequence().map { step ->
            createTest(domain, step)
        }

    private fun createTest(
        domainUnderTest: D,
        step: DdtStep<D>
    ): DynamicNode =
        dynamicTest(decorateTestName(domainUnderTest, step), step.testSourceURI()) {
            execute(domainUnderTest, step)  //TODO do a fold so that domain will come from the previous test
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
                "Unexpected Exception while initializing the tests. Have you forgotten to use executeStep in your steps? ",
                t
            )
        }

    fun DomainUnderTest<*>.description(): String = "${javaClass.simpleName} - ${protocol.desc}"
}