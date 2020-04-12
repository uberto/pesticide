package com.ubertob.pesticide

import anura.ddts.Scenario
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


abstract class DomainDrivenTest<D : DomainUnderTest<*>>(val domains: Sequence<D>) {

    fun play(vararg stepsArray: DdtStep<D>): ScenarioSteps<D> =
        ScenarioSteps(stepsArray.toList())

    fun ScenarioSteps<D>.wip(
        dueDate: LocalDate,
        reason: String = "Work In Progress",
        except: Set<KClass<out DdtProtocol>> = emptySet()
    ): ScenarioSteps<D> =
        this.copy(WipData = WipData(dueDate, except, reason))

    val timeoutInMillis = 1000

    fun ddtScenario(
        block: D.() -> Scenario<D>
    ): Stream<out DynamicNode> =
        rootContext {
            domains.forEach { domain ->
                context("running ${domain.description()}") {
                    assertTrue(domain.isReady(), "Protocol ${domain.protocol.desc} ready")

                    trapUnexpectedExceptions {
                        block(domain)
                            .steps.runTests(this, domain)
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

    val withoutSetting = DdtStep<D>("empty stage") { it }

    fun <D : DomainUnderTest<*>> D.setting(
        block: D.() -> D
    ): DdtStep<D> =
        DdtStep("Preparing", block)


    infix fun <D : DomainUnderTest<*>> DdtStep<D>.atRise(steps: ScenarioSteps<D>): Scenario<D> =
        Scenario(
            "scenario",
            ScenarioSteps(listOf(this) + steps.steps, steps.WipData)
        )

    fun DomainUnderTest<*>.description(): String = "${javaClass.simpleName} - ${protocol.desc}"

}

class ActorDelegate<D : DomainUnderTest<*>, A : DdtActor<D>>(val actorConstructor: (String) -> A) :
    ReadOnlyProperty<DomainDrivenTest<D>, A> {
    override operator fun getValue(thisRef: DomainDrivenTest<D>, property: KProperty<*>): A =
        actorConstructor(property.name.capitalize())

}