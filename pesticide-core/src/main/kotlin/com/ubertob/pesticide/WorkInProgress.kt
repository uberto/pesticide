package com.ubertob.pesticide

import dev.minutest.TestDescriptor
import org.junit.jupiter.api.fail
import org.opentest4j.TestAbortedException
import java.time.LocalDate

data class TestAbortedExceptionWIP(override val message: String, val throwable: Throwable? = null) :
    TestAbortedException(message, throwable)


fun <F> wip(
    due: LocalDate,
    f: () -> Unit
): F.(testDescriptor: TestDescriptor) -> Unit = {
    if (due < LocalDate.now()) {
        fail("Due date expired $due")
    } else {
        try {
            f()
        } catch (t: Throwable) {
            //all ok
            throw TestAbortedExceptionWIP(
                "Test failed but this is ok until $due",
                t
            )
        }
        throw TestAbortedExceptionWIP("Test succeded but you have to remove the WIP marker!")
    }
}
