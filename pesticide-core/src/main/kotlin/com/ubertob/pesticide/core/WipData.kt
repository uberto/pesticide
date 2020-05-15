package com.ubertob.pesticide.core

import java.time.LocalDate
import kotlin.reflect.KClass

data class WipData(val dueDate: LocalDate, val envWhereItShouldWork: Set<KClass<out DdtProtocol>>, val reason: String) {

    fun shouldWorkFor(environment: DdtProtocol): Boolean = environment::class in envWhereItShouldWork
}