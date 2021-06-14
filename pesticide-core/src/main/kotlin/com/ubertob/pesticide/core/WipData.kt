package com.ubertob.pesticide.core

import java.time.LocalDate
import kotlin.reflect.KClass

/**
 * WipData is the class to keep together the information for WorkInProgress test.
 * Normally it shouldn't be created directly but using the {@code wip()} extension of the UseCase inside the DDT.
 *
 * See also {@link DdtScenario} and {@link DomainDrivenTest}
 */
data class WipData(val dueDate: LocalDate, val envWhereItShouldWork: Set<KClass<out DdtProtocol>>, val reason: String) {

    fun shouldWorkFor(environment: DdtProtocol): Boolean = environment::class in envWhereItShouldWork
}