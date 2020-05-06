package com.ubertob.pesticide.examples.stack;

import com.ubertob.pesticide.DomainDrivenTest;
import com.ubertob.pesticide.examples.stack.testing.StackInterpreter;
import com.ubertob.pesticide.examples.stack.testing.StackUser;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.stream.Stream;


public class StackDDT extends DomainDrivenTest<StackInterpreter> {

    public StackDDT() {
        super(StackInterpreter.allProtocols());
    }

    StackUser sabine = new StackUser("Sabine");

    @TestFactory
    public Stream<DynamicContainer> push3Numbers() {
        return ddtScenario(() -> atRise(
                withoutSetting,
                play(
                        sabine.pushANumber(4),
                        sabine.pushANumber(5),
                        sabine.pushANumber(6),
                        sabine.verifyStackSizeIs(3)
                )
        ));
    }

    @TestFactory
    public Stream<DynamicContainer> pushAndPull() {
        return ddtScenario(() -> atRise(
                withoutSetting,
                play(
                        sabine.pushANumber(4),
                        sabine.pushANumber(5),
                        sabine.pushANumber(6),
                        sabine.popANumber(6),
                        sabine.popANumber(5),
                        sabine.popANumber(4),
                        sabine.verifyStackSizeIs(0)
                )
        ));
    }


    @TestFactory
    public Stream<DynamicContainer> testWorkInProgress() {
        return ddtScenario(() -> atRise(
                setting(d -> {
                    d.pushNumber(5);
                    return d;
                }),
                wip(
                        play(sabine.popANumber(4)),
                        LocalDate.of(2100, 01, 01), "Impossible Stack", new HashSet<>()
                )
        ));
    }


}
