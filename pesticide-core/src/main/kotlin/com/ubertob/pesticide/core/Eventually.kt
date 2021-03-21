package com.ubertob.pesticide.core

import java.time.Clock
import java.time.Duration


//Note expectation should be an operation that takes less then the within duration
inline fun <T> eventually(within: Duration, expectation: () -> T): T {

    val intervals: Iterator<Long> = fibonacci().iterator()
    val chrono = Chrono()
    while (true) {
        try {
            return expectation()
        } catch (t: Throwable) {
            if (chrono.hasTimedOut(within))
                throw t
        }
        Thread.sleep(intervals.next())
    }
}

fun fibonacci(): Sequence<Long> = generateSequence(1L to 1L, { it.second to (it.second + it.first) }).map { it.first }


data class Chrono(val startedMillis: Long = System.currentTimeMillis(), val clock: Clock = Clock.systemUTC()) {
    fun elapsedMillis(): Long = clock.millis() - startedMillis

    fun isWithin(duration: Duration): Boolean = elapsedMillis() <= duration.toMillis()
    fun hasTimedOut(duration: Duration): Boolean = !isWithin(duration)
}