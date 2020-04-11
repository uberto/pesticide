package com.ubertob.pesticide

import dev.minutest.ContextBuilder
import dev.minutest.NodeBuilder
import dev.minutest.TestDescriptor
import org.junit.jupiter.api.fail
import org.opentest4j.TestAbortedException
import java.time.LocalDate

class TestAbortedExceptionWIP(override val message: String, val throwable: Throwable? = null) :
    TestAbortedException(message, throwable)

fun <F> ContextBuilder<F>.testWIP(
    name: String,
    due: LocalDate,
    f: F.(testDescriptor: TestDescriptor) -> Unit
): NodeBuilder<F> = test(name, wip(due, f))


fun <F> wip(
    due: LocalDate,
    f: F.(testDescriptor: TestDescriptor) -> Unit
): F.(testDescriptor: TestDescriptor) -> Unit =
    if (due < LocalDate.now()) {
        fail("Due date expired $due")
    } else { fixture ->
        try {
            f(fixture)
        } catch (t: Throwable) {
            //all ok
            throw TestAbortedExceptionWIP(
                "Test failed but this is ok until $due",
                t
            )
        }
        throw TestAbortedExceptionWIP("Test succeded but you have to remove the WIP marker!")
    }
