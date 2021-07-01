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


/**
 * DomainDrivenTest is the base class to inherit to create a test.
 * A test should look something like:
 *
 * <pre>
 *    class MyDDT : DomainDrivenTest<MyActions>(actionsForAllProtocols()) {
 *
 *   val adam by NamedUser(::MyAUser)
 *
 *   @DDT
 *   fun `do something`() = useCase {
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

abstract class DomainDrivenTest<D : DdtActions<*>>(private val domains: Iterable<D>) {

    fun play(vararg stepsArray: DdtStep<D, *>): DdtUseCase<D> =
        DdtUseCase(withoutSetting, stepsArray.toList())

    /**
     * wip
     *
     * Mark a test as Work In Progress until a given date. It's possible to specify that some protocols are not in WIP (that is the test should work)
     */
    fun DdtUseCase<D>.wip(
        dueDate: LocalDate,
        reason: String = "Work In Progress",
        except: Set<KClass<out DdtProtocol>> = emptySet()
    ): DdtUseCase<D> =
        this.copy(wipData = WipData(dueDate, except, reason))

    /**
     * ddtScenario
     *
     * Define a use case for a test.
     * Example of use:
     *
     *  <pre>
     *   @DDT
     *   fun `do something`() = useCase { protocol ->
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
    fun useCase(
        useCaseBuilder: (DdtProtocol) -> DdtUseCase<D>
    ): Stream<DynamicContainer> =
        domains.map { useCaseBuilder(it.protocol)(it) }
            .ifEmpty { fail("No protocols selected!") }
            .stream()

    //for retro-compatibility
    fun ddtScenario(
        useCaseBuilder: (DdtProtocol) -> DdtUseCase<D>
    ): Stream<DynamicContainer> = useCase(useCaseBuilder)


    @JvmField
    val withoutSetting: DdtSetup<D> =
        DdtSetup()

    fun setting(block: D.() -> Unit): DdtSetup<D> =
        DdtSetup {
            block(this)
        }


    fun setUp(block: D.() -> Unit): DdtSetup<D> =
        DdtSetup {
            block(this)
        }

    fun DdtSetup<D>.thenPlay(vararg stepsArray: DdtStep<D, *>): DdtUseCase<D> =
        DdtUseCase(this, stepsArray.toList())

    //useful for Java
    fun onSetUp(block: Consumer<D>): DdtSetup<D> = setUp { block.accept(this) }

    infix fun DdtSetup<D>.atRise(steps: DdtUseCase<D>): DdtUseCase<D> =
        steps.copy(ddtSetup = this)


    class NamedUser<D : DdtActions<*>, A : DdtUserWithContext<in D, *>>(val userConstructor: (String) -> A) :
        ReadOnlyProperty<DomainDrivenTest<D>, A> {
        override operator fun getValue(thisRef: DomainDrivenTest<D>, property: KProperty<*>): A =
            userConstructor(property.name.capitalize())
    }

}
