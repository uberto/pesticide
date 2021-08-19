package com.ubertob.pesticide.examples.stack.testing;


import com.ubertob.pesticide.core.DdtActions;
import com.ubertob.pesticide.core.DdtProtocol;

import java.util.ArrayList;
import java.util.List;

public interface StackActions extends DdtActions<DdtProtocol> {
    void pushNumber(int num);

    int popNumber();

    int size();

    static Iterable<StackActions> allProtocols() {
        List<StackActions> domains = new ArrayList<>();
        domains.add(new StackActionsFakeHttp());
        domains.add(new StackActionsInMemory());
        return domains;
    }
}
