package com.ubertob.pesticide

import dev.minutest.junit.toTestFactory
import dev.minutest.rootContext
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.fail
import java.time.LocalDate
import java.util.stream.Stream
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


typealias DDT = TestFactory

data class Setting<D : DomainUnderTest<*>>(val setUp: DdtStep<D>)

abstract class DomainDrivenTest<D : DomainUnderTest<*>>(val domains: Sequence<D>) {

    fun play(vararg stepsArray: DdtStep<D>): Scenario<D> =
        Scenario(stepsArray.toList())

    fun Scenario<D>.wip(
        dueDate: LocalDate,
        reason: String = "Work In Progress",
        except: Set<KClass<out DdtProtocol>> = emptySet()
    ): Scenario<D> =
        this.copy(wipData = WipData(dueDate, except, reason))

    val timeoutInMillis = 1000


    fun ddtScenario(
        block: D.() -> Scenario<D>
    ): Stream<out DynamicNode> =
        rootContext {
            domains.forEach { domain ->
                context("running ${domain.description()}") {
                    assertTrue(domain.isReady(), "Protocol ${domain.protocol.desc} ready")

                    trapUnexpectedExceptions {
                        block(domain).runTests(this, domain)
                    }
                }
            }
        }.toTestFactory()

    private fun trapUnexpectedExceptions(block: () -> Unit) =
        try {
            block()
        } catch (t: Throwable) {
            fail(
                "Unexpected Exception while initializing the tests. Have you forgotten to use executeStep in your steps? ",
                t
            )
        }

    val D.withoutSetting: Setting<D>
        get() = Setting(DdtStep("empty stage") { it })

    fun <D : DomainUnderTest<*>> D.setting(
        block: D.() -> D
    ): Setting<D> = Setting(DdtStep("Preparing", block))


    infix fun Setting<D>.atRise(steps: Scenario<D>): Scenario<D> =
        Scenario(listOf(this.setUp) + steps.steps, steps.wipData)

    fun DomainUnderTest<*>.description(): String = "${javaClass.simpleName} - ${protocol.desc}"

}

class ActorDelegate<D : DomainUnderTest<*>, A : DdtActor<D>>(val actorConstructor: (String) -> A) :
    ReadOnlyProperty<DomainDrivenTest<D>, A> {
    override operator fun getValue(thisRef: DomainDrivenTest<D>, property: KProperty<*>): A =
        actorConstructor(property.name.capitalize())

}