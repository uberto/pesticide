package com.ubertob.pesticide.ddt.core

import com.ubertob.pesticide.core.Chrono
import com.ubertob.pesticide.core.eventually
import com.ubertob.pesticide.core.increasing
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.*
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

class EventuallyTest {

    @Test
    fun `increasing function`() {
        val seq = increasing().take(20).toList()

        expectThat(seq).isEqualTo(
            listOf(5, 7, 10, 14, 20, 28, 40, 56, 79, 111, 156, 219, 307, 430, 602, 843, 1181, 1000, 1000, 1000)
        )
    }

    @Test
    fun `eventually fails if the condition is not met`() {

        val c = Chrono()
        var timeoutException: Throwable? = null
        try {
            eventually(Duration.ofMillis(100)) {
                expectThat(1).isEqualTo(2)
            }
        } catch (t: Throwable) {
            timeoutException = t
        }
        expectThat(timeoutException?.message).isNotNull().contains("is equal to 2")
        expectThat(c.elapsedMillis()).isGreaterThan(100)

    }

    @Test
    fun `eventually pass immediately if the condition is met`() {

        val c = Chrono()
        var timeoutException: Exception? = null
        try {
            eventually(Duration.ofMillis(100)) {
                expectThat(2).isEqualTo(2)
            }
        } catch (e: Exception) {
            timeoutException = e
        }
        expectThat(timeoutException).isNull()
        expectThat(c.elapsedMillis()).isLessThan(10)

    }

    @Test
    fun `eventually keep checking the condition until the timeout`() {

        val c = Chrono()
        val counter = AtomicInteger(0)
        var timeoutException: Exception? = null
        try {
            eventually(Duration.ofMillis(100)) {
                expectThat(counter.incrementAndGet()).isEqualTo(5)
            }
        } catch (e: Exception) {
            timeoutException = e
        }

        expectThat(timeoutException).isNull()
        expectThat(counter.get()).isEqualTo(5)
        expectThat(c.elapsedMillis()).isLessThan(100)

    }
}