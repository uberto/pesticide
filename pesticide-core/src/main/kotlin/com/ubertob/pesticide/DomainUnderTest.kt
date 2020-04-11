package com.ubertob.pesticide

interface DomainUnderTest<out P : DdtProtocol> {
    val protocol: P

    fun isStarted(): Boolean
}


