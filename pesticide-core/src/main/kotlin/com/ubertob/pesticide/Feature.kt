package com.ubertob.pesticide

import anura.ddts.Scenario
import dev.minutest.TestContextBuilder

data class Feature<D : DomainUnderTest<*>>(
    val domain: D,
    val testContextBuilder: TestContextBuilder<Unit, Unit>,
    val wipData: WipData? = null
) {

    fun Scenario<D>.runAsSingle() {
        steps.runTests(testContextBuilder, domain)
    }


}