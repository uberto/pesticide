package com.ubertob.pesticide

import org.junit.jupiter.api.fail
import org.opentest4j.TestAbortedException
import java.time.LocalDate

data class TestAbortedExceptionWIP(override val message: String, val throwable: Throwable? = null) :
    TestAbortedException(message, throwable)


fun <D : DomainUnderTest<*>> executeInWIP(
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
    } //TODO this should be raised at Scenario level, not single step. If a test succeed before WIP shouldn't hide a failing one.
}