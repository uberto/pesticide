package com.ubertob.pesticide

import org.junit.jupiter.api.DynamicContainer
import org.junit.jupiter.api.TestFactory
import java.time.LocalDate
import java.util.stream.Stream
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


typealias DDT = TestFactory

data class Setting<D : BoundedContextInterpreter<*>>(val setUp: DdtStep<D, Unit>)


class FakeActor<D : BoundedContextInterpreter<*>> : DdtActor<D>() {
    override val name: String = "Fake"
}

abstract class DomainDrivenTest<D : BoundedContextInterpreter<*>>(private val domains: Iterable<D>) {

    val fakeActor = FakeActor<D>()

    fun play(vararg stepsArray: DdtStep<D, *>): Scenario<D> =
        Scenario(stepsArray.toList())

    fun Scenario<D>.wip(
        dueDate: LocalDate,
        reason: String = "Work In Progress",
        except: Set<KClass<out DdtProtocol>> = emptySet()
    ): Scenario<D> =
        this.copy(wipData = WipData(dueDate, except, reason))

    val timeoutInMillis = 1000


    fun ddtScenario(
        scenarioBuilder: () -> Scenario<D>
    ): Stream<DynamicContainer> =
        domains.map {
            scenarioBuilder()(it)
        }.toList().stream()


    @JvmField
    val withoutSetting: Setting<D> = Setting(DdtStep(fakeActor, "Empty scenario") { it })

    fun setting(
        block: D.() -> D
    ): Setting<D> = Setting(DdtStep(fakeActor, "Setting up the scenario") {
        block(this)
        Unit
    })


    infix fun Setting<D>.atRise(steps: Scenario<D>): Scenario<D> =
        Scenario(listOf(this.setUp) + steps.steps, steps.wipData) //add source URL

}


class NamedActor<D : BoundedContextInterpreter<*>, A : DdtActor<D>>(val actorConstructor: (String) -> A) :
    ReadOnlyProperty<DomainDrivenTest<D>, A> {
    override operator fun getValue(thisRef: DomainDrivenTest<D>, property: KProperty<*>): A =
        actorConstructor(property.name.capitalize())

}