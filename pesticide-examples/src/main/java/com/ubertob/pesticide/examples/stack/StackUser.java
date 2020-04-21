package com.ubertob.pesticide.examples.stack;

import com.ubertob.pesticide.DdtActor;
import com.ubertob.pesticide.DdtStep;
import kotlin.Unit;

import static org.assertj.core.api.Assertions.assertThat;

public class StackUser implements DdtActor<StackDomain> {

    private final String name;

    StackUser(String name) {
        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }

    DdtStep<StackDomain> pushANumber(int x) {
        return generateStep(domain -> {
            int size = domain.size();
            domain.pushNumber(x);

            assertThat(domain.size()).isEqualTo(size + 1);
            return Unit.INSTANCE;
        });
    }

    DdtStep<StackDomain> popANumber(int expected) {
        return generateStep(domain -> {
                    int x = domain.popNumber();

                    assertThat(x).isEqualTo(expected);
                    return Unit.INSTANCE;
                }
        );
    }
}

