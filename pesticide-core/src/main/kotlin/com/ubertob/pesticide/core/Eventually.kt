package com.ubertob.pesticide.core

import java.time.Clock
import java.time.Duration


//Note expectation should be an operation that takes less then the within duration
inline fun <T> eventually(within: Duration, expectation: () -> T): T =
    eventually(within, ::increasing, expectation)

inline fun <T> eventually(within: Duration, increasingFn: () -> Sequence<Long>, expectation: () -> T): T {

    val intervals: Iterator<Long> = increasingFn().iterator()
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

fun increasing(): Sequence<Long> = generateSequence(5L)
{ x ->
    if (x >= 1000)
        1000L
    else
        Math.ceil(x * 1.4).toLong()
}


data class Chrono(val startedMillis: Long = System.currentTimeMillis(), val clock: Clock = Clock.systemUTC()) {
    fun elapsedMillis(): Long = clock.millis() - startedMillis

    fun isWithin(duration: Duration): Boolean = elapsedMillis() <= duration.toMillis()
    fun hasTimedOut(duration: Duration): Boolean = !isWithin(duration)
}