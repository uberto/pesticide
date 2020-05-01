package com.ubertob.pesticide.examples.stack;

import com.ubertob.pesticide.DdtActor;
import com.ubertob.pesticide.DdtStep;

import static org.assertj.core.api.Assertions.assertThat;

public class StackUser extends DdtActor<StackDomain> {

    private final String name;

    StackUser(String name) {
        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }

    DdtStep<StackDomain> pushANumber(int x) {
        return stepWithDesc("push " + x + " into stack", domain -> {
            int size = domain.size();
            domain.pushNumber(x);

            assertThat(domain.size()).isEqualTo(size + 1);
        });
    }

    DdtStep<StackDomain> popANumber(int expected) {
        return stepWithDesc("pop from stack and expect " + expected, domain -> {
                    int x = domain.popNumber();

                    assertThat(x).isEqualTo(expected);
                }
        );
    }

    DdtStep<StackDomain> verifyStackSizeIs(int expected) {
        return stepWithDesc("verify stack size is " + expected,
                domain -> {
                    assertThat(domain.size()).isEqualTo(expected);
                }
        );
    }
}

