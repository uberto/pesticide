package com.ubertob.pesticide

import dev.minutest.NodeBuilder
import dev.minutest.TestContextBuilder
import org.junit.jupiter.api.Assumptions

data class ScenarioSteps<D : DomainUnderTest<*>>(val steps: List<DdtStep<D>>, val WipData: WipData? = null) {

    var alreadyFailed = false //dirty but it works

    fun runTests(testContextBuilder: TestContextBuilder<*, *>, domainUnderTest: D) =
        steps.map { step ->
            testContextBuilder.tryTest(domainUnderTest, decorateTestName(domainUnderTest, step)) {
                Assumptions.assumeFalse(alreadyFailed, "Skipped because of previous failures")

                try {
                    step.runnable(domainUnderTest)
                } catch (t: Throwable) {
                    alreadyFailed = true
                    throw t
                }

            }
        }

    private fun decorateTestName(domainUnderTest: D, step: DdtStep<D>) =
        "${domainUnderTest.protocol.desc} - ${step.description}"

    private fun TestContextBuilder<*, *>.tryTest(
        domain: D,
        name: String,
        testBlock: () -> Unit
    ): NodeBuilder<out Any?> =
        if (WipData == null || WipData.shouldWorkFor(domain.protocol))
            test(name) {
                testBlock()
            }
        else
            testWIP(name, WipData.dueDate) {
                testBlock()
            }
}