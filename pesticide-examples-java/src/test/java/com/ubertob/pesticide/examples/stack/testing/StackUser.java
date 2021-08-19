package com.ubertob.pesticide.examples.stack.testing;

import com.ubertob.pesticide.core.DdtActor;
import com.ubertob.pesticide.core.DdtStep;
import kotlin.Unit;

import static org.assertj.core.api.Assertions.assertThat;

public class StackUser extends DdtActor<StackActions> {

    private final String name;

    public StackUser(String name) {
        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }

    public DdtStep<StackActions, Unit> pushANumber(int x) {
        return stepWithDesc("push " + x + " into stack", domain -> {
            int size = domain.size();
            domain.pushNumber(x);

            assertThat(domain.size()).isEqualTo(size + 1);
        });
    }

    public DdtStep<StackActions, Unit> popANumber(int expected) {
        return stepWithDesc("pop from stack and expect " + expected, domain -> {
                    int x = domain.popNumber();

                    assertThat(x).isEqualTo(expected);
                }
        );
    }

    public DdtStep<StackActions, Unit> verifyStackSizeIs(int expected) {
        return stepWithDesc("verify stack size is " + expected,
                domain -> {
                    assertThat(domain.size()).isEqualTo(expected);
                }
        );
    }
}

