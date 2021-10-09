package com.ubertob.pesticide.core

import org.junit.jupiter.api.AfterEach
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


/**
 * DomainDrivenTest is the base class to inherit to create a test.
 * A test should look something like:
 *
 * <pre>
 *    class MyDDT : DomainDrivenTest<MyActions>(actionsForAllProtocols()) {
 *
 *   val adam by NamedActor(::MyAUser)
 *
 *   @DDT
 *   fun `do something`() = ddtScenario {
 *       setup {
 *         preparation()
 *       }.thenPlay(
 *           adam.`do this`()
 *           adam.`do that`()
 *       }
 *   }
 *
 * </pre>
 *
 * See also {@link DdtScenario}
 */

abstract class DomainDrivenTest<D : DdtActions<*>>(private val actionsSet: Iterable<D>) {

    fun play(vararg stepsArray: DdtStep<D, *>): DdtScenario<D> =
        DdtScenario(DdtSetup(), stepsArray.toList())

    /**
     * wip
     *
     * Mark a test as Work In Progress until a given date. It's possible to specify that some protocols are not in WIP (that is the test should work)
     */
    fun DdtScenario<D>.wip(
        dueDate: LocalDate,
        reason: String = "Work In Progress",
        except: Set<KClass<out DdtProtocol>> = emptySet()
    ): DdtScenario<D> =
        this.copy(wipData = WipData(dueDate, except, reason))

    /**
     * ddtScenario
     *
     * Define a use case for a test.
     * Example of use:
     *
     *  <pre>
     *   @DDT
     *   fun `do something`() = ddtScenario { protocol ->
     *
     *       val secret = getCredentials(protocol)
     *
     *       setUp {
     *         preparation()
     *       }.thenPlay(
     *           adam.`can do this`(secret)
     *           adam.`can do that`()
     *       }
     *   }
     *
     * </pre>
     */

    fun ddtScenario(
        scenarioBuilder: (DdtProtocol) -> DdtScenario<D>
    ): Stream<DynamicContainer> =
        actionsSet.map { actions -> scenarioBuilder(actions.protocol)(actions) }
            .ifEmpty { fail("No protocols selected!") }
            .stream()

    @AfterEach
    fun pullDownActions() {
        actionsSet.onEach { it.tearDown() }
    }

    @Deprecated("Use play() directly")
    @JvmField
    val withoutSetting: DdtSetup<D> =
        DdtSetup()

    @Deprecated("Use setUp() method instead")
    fun setting(block: D.() -> Unit): DdtSetup<D> =
        DdtSetup {
            block(this)
        }


    fun setUp(block: D.() -> Unit): DdtSetup<D> =
        DdtSetup {
            block(this)
        }

    fun DdtSetup<D>.thenPlay(vararg stepsArray: DdtStep<D, *>): DdtScenario<D> =
        DdtScenario(this, stepsArray.toList())

    //useful for Java
    fun onSetUp(block: Consumer<D>): DdtSetup<D> = setUp { block.accept(this) }

    infix fun DdtSetup<D>.atRise(steps: DdtScenario<D>): DdtScenario<D> =
        steps.copy(ddtSetup = this)


    class NamedActor<D : DdtActions<*>, A : DdtActorWithContext<in D, *>>(val userConstructor: (String) -> A) :
        ReadOnlyProperty<DomainDrivenTest<D>, A> {
        override operator fun getValue(thisRef: DomainDrivenTest<D>, property: KProperty<*>): A =
            userConstructor(property.name.replaceFirstChar { it.titlecase() })
    }

}
