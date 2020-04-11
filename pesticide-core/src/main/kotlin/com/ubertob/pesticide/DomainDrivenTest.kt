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
import kotlin.reflect.KClass


/**
 *
 * extend this class and write tests in this way:
 *
 *    @DDT
 *    fun myTest() =
 *        ddtFeature {
 *            set up stuff
 *            [WIP().]scenario(...[steps])
 *        }
 *
 */

typealias DDT = TestFactory


abstract class DomainDrivenTest<D : DomainUnderTest<*>>(val domains: Sequence<D>) {

    fun steps(vararg stepsArray: DdtStep<D>): ScenarioSteps<D> =
        ScenarioSteps(stepsArray.toList())

    fun Feature<D>.WIP(
        dueDate: LocalDate,
        reason: String = "Work In Progress",
        except: Set<KClass<out DdtProtocol>> = emptySet()
    ): Feature<D> =
        this.copy(wipData = WipData(dueDate, except, reason))

    fun ScenarioSteps<D>.wip(
        dueDate: LocalDate,
        reason: String = "Work In Progress",
        except: Set<KClass<out DdtProtocol>> = emptySet()
    ): ScenarioSteps<D> =
        this.copy(WipData = WipData(dueDate, except, reason))

    fun Feature<D>.scenario(vararg steps: DdtStep<D>) =
        Scenario("scenario", ScenarioSteps(steps.toList(), wipData)).runAsSingle()

    val timeoutInMillis = 1000

    fun ddtScenario(
        block: D.() -> Scenario<D>
    ): Stream<out DynamicNode> =
        rootContext {
            domains.forEach { domain ->
                context("running ${domain.description()}") {
                    assertTrue(domain.isStarted(), "Protocol ${domain.protocol.desc} has started")

                    trapUnexpectedExceptions {
                        block(domain)
                            .steps.runTests(this, domain)
                    }
                }
            }
        }.toTestFactory()

// probably not needed
//
//    fun <F:Any> ddtScenarioWithFixture(
//        myFixture: F,
//        block: D.(F) -> Scenario<D>
//    ): Stream<out DynamicNode> =
//        rootContext {
//            domains.forEach { domain ->
//                context("running ${domain.description()}") {
//
//                    expectThat(domain.isStarted()).describedAs("Protocol ${domain.protocol.desc} has started").isTrue()
//
//                    trapUnexpectedExceptions {
//                        block(domain, myFixture)
//                            .steps.runTests(this, domain)
//                    }
//                }
//            }
//        }.toTestFactory()

    private fun trapUnexpectedExceptions(block: () -> Unit) =
        try {
            block()
        } catch (t: Throwable) {
            fail(
                "Unexpected Exception while initializing the tests. Have you forgotten to use executeStep in your steps? ",
                t
            )
        }

    val onEmptyStage = DdtStep<D>("empty stage") { it }

    fun <D : DomainUnderTest<*>> D.onStage(
        block: D.() -> D
    ): DdtStep<D> =
        DdtStep("Preparing", block)


    infix fun <D : DomainUnderTest<*>> DdtStep<D>.actorsPlay(steps: ScenarioSteps<D>): Scenario<D> =
        Scenario(
            "scenario",
            ScenarioSteps(listOf(this) + steps.steps, steps.WipData)
        )


    fun DomainUnderTest<*>.description(): String = "${javaClass.simpleName} - ${protocol.desc}"


}

