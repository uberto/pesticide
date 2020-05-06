package com.ubertob.pesticide.examples.stack.testing;

import com.ubertob.pesticide.DdtActor;
import com.ubertob.pesticide.DdtStep;

import static org.assertj.core.api.Assertions.assertThat;

public class StackUser extends DdtActor<StackInterpreter> {

    private final String name;

    public StackUser(String name) {
        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }

    public DdtStep<StackInterpreter> pushANumber(int x) {
        return stepWithDesc("push " + x + " into stack", domain -> {
            int size = domain.size();
            domain.pushNumber(x);

            assertThat(domain.size()).isEqualTo(size + 1);
        });
    }

    public DdtStep<StackInterpreter> popANumber(int expected) {
        return stepWithDesc("pop from stack and expect " + expected, domain -> {
                    int x = domain.popNumber();

                    assertThat(x).isEqualTo(expected);
                }
        );
    }

    public DdtStep<StackInterpreter> verifyStackSizeIs(int expected) {
        return stepWithDesc("verify stack size is " + expected,
                domain -> {
                    assertThat(domain.size()).isEqualTo(expected);
                }
        );
    }
}

