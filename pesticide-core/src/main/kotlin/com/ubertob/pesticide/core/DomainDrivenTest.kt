package com.ubertob.pesticide.core

import org.junit.jupiter.api.DynamicContainer
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.fail
import java.time.LocalDate
import java.util.function.Consumer
import java.util.stream.Stream
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


typealias DDT = TestFactory

abstract class DomainDrivenTest<D : DomainInterpreter<*>>(private val domains: Iterable<D>) {

    fun play(vararg stepsArray: DdtStep<D, *>): DdtScenario<D> =
        DdtScenario(withoutSetting, stepsArray.toList())

    fun DdtScenario<D>.wip(
        dueDate: LocalDate,
        reason: String = "Work In Progress",
        except: Set<KClass<out DdtProtocol>> = emptySet()
    ): DdtScenario<D> =
        this.copy(wipData = WipData(dueDate, except, reason))

    val timeoutInMillis = 1000


    fun ddtScenario(
        scenarioBuilder: () -> DdtScenario<D>
    ): Stream<DynamicContainer> =
        domains.map(scenarioBuilder())
            .ifEmpty { fail("No protocols selected!") }
            .stream()


    @JvmField
    val withoutSetting: Setting<D> =
        Setting()

    fun setting(block: D.() -> Unit): Setting<D> =
        Setting {
            block(this)
        }

    //useful for Java
    fun onSetting(block: Consumer<D>): Setting<D> = setting { block.accept(this) }

    infix fun Setting<D>.atRise(steps: DdtScenario<D>): DdtScenario<D> =
        steps.copy(setting = this)

    class NamedActor<D : DomainInterpreter<*>, A : DdtActorWithContext<D, *>>(val actorConstructor: (String) -> A) :
        ReadOnlyProperty<DomainDrivenTest<D>, A> {
        override operator fun getValue(thisRef: DomainDrivenTest<D>, property: KProperty<*>): A =
            actorConstructor(property.name.capitalize())
    }
}


