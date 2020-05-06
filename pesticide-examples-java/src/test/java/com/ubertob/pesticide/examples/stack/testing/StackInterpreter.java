package com.ubertob.pesticide.examples.stack.testing;


import com.ubertob.pesticide.BoundedContextInterpreter;
import com.ubertob.pesticide.DdtProtocol;

import java.util.ArrayList;
import java.util.List;

public interface StackInterpreter extends BoundedContextInterpreter<DdtProtocol> {
    void pushNumber(int num);

    int popNumber();

    int size();

    static Iterable<StackInterpreter> allProtocols() {
        List<StackInterpreter> domains = new ArrayList<>();
        domains.add(new StackInterpreterFakeHttp());
        domains.add(new StackInterpreterInMemory());
        return domains;
    }
}
