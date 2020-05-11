package com.ubertob.pesticide

import org.junit.jupiter.api.DynamicContainer
import org.junit.jupiter.api.TestFactory
import java.time.LocalDate
import java.util.function.Consumer
import java.util.stream.Stream
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


typealias DDT = TestFactory

data class Setting<D : DomainInterpreter<*>>(val setUp: DdtStep<D, Unit>)


class FakeActor<D : DomainInterpreter<*>> : DdtActor<D>() {
    override val name: String = "Fake"
}

abstract class DomainDrivenTest<D : DomainInterpreter<*>>(private val domains: Iterable<D>) {

    val fakeActor = FakeActor<D>()

    fun play(vararg stepsArray: DdtStep<D, *>): DdtScenario<D> =
        DdtScenario(stepsArray.toList())

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
        domains.map {
            scenarioBuilder()(it)
        }.toList().stream()


    @JvmField
    val withoutSetting: Setting<D> = Setting(DdtStep(fakeActor, "Empty scenario") {})

    fun setting(block: D.() -> Unit): Setting<D> =
        Setting(DdtStep(fakeActor, "Setting up the scenario") {
            block(this)
        })

    //useful for Java
    fun onSetting(block: Consumer<D>): Setting<D> = setting { block.accept(this) }

    infix fun Setting<D>.atRise(steps: DdtScenario<D>): DdtScenario<D> =
        DdtScenario(listOf(this.setUp) + steps.steps, steps.wipData) //add source URL

}


class NamedActor<D : DomainInterpreter<*>, A : DdtActorWithContext<D, *>>(val actorConstructor: (String) -> A) :
    ReadOnlyProperty<DomainDrivenTest<D>, A> {
    override operator fun getValue(thisRef: DomainDrivenTest<D>, property: KProperty<*>): A =
        actorConstructor(property.name.capitalize())
}