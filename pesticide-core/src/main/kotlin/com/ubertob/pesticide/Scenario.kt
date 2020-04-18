package com.ubertob.pesticide

import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest


data class Scenario<D : DomainUnderTest<*>>(val steps: Iterable<DdtStep<D>>, val wipData: WipData? = null) {

    var alreadyFailed = false //TODO replace it with a proper fold

    fun createTests(domainUnderTest: D): Sequence<DynamicNode> =
        steps.asSequence().map { step ->
            createTest(domainUnderTest, step)
        }

    private fun createTest(
        domainUnderTest: D,
        step: DdtStep<D>
    ): DynamicNode =
        dynamicTest(decorateTestName(domainUnderTest, step), step.testSourceURI()) {
            execute(domainUnderTest, step)
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
        "${domainUnderTest.protocol.desc} - ${step.description} ${step.testSourceURI()}"


    private fun checkWIP(wipData: WipData?, domain: D, testBlock: (D) -> D): D =
        if (wipData == null || wipData.shouldWorkFor(domain.protocol))
            testBlock(domain)
        else
            executeInWIP(wipData.dueDate, testBlock)(domain)


}