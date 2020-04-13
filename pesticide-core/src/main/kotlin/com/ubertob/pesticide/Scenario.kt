package com.ubertob.pesticide

import dev.minutest.NodeBuilder
import dev.minutest.TestContextBuilder
import org.junit.jupiter.api.Assumptions

data class Scenario<D : DomainUnderTest<*>>(val steps: List<DdtStep<D>>, val wipData: WipData? = null) {

    var alreadyFailed = false //dirty but it works

    fun runTests(testContextBuilder: TestContextBuilder<*, *>, domainUnderTest: D) =
        steps.forEach { step ->

            try {
                tryStep(testContextBuilder, domainUnderTest, step)
            } catch (t: Throwable) {
                println("got it!! $t")

            }
        }

    private fun tryStep(
        testContextBuilder: TestContextBuilder<*, *>,
        domainUnderTest: D,
        step: DdtStep<D>
    ): NodeBuilder<out Any?> =
//        testContextBuilder.context(decorateTestName(domainUnderTest, step)) {
        testContextBuilder.wipTest(domainUnderTest, decorateTestName(domainUnderTest, step)) {
            Assumptions.assumeFalse(alreadyFailed, "Skipped because of previous failures")

            try {
                step.action(domainUnderTest)
            } catch (t: Throwable) {
                alreadyFailed = true
                throw t
            }

        }

    private fun decorateTestName(domainUnderTest: D, step: DdtStep<D>) =
        "${domainUnderTest.protocol.desc} - ${step.description}"


    private fun TestContextBuilder<*, *>.wipTest(
        domain: D,
        name: String,
        testBlock: () -> Unit
    ): NodeBuilder<*> =
        test(name, if (wipData == null || wipData.shouldWorkFor(domain.protocol))
            { _ -> testBlock() }
        else
            wip(wipData.dueDate, testBlock)
        )

}