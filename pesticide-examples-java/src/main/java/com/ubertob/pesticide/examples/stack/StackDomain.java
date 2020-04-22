package com.ubertob.pesticide.examples.stack;


import com.ubertob.pesticide.DdtProtocol;
import com.ubertob.pesticide.DomainUnderTest;

import java.util.ArrayList;
import java.util.List;

interface StackDomain extends DomainUnderTest<DdtProtocol> {
    void pushNumber(int num);

    int popNumber();

    int size();

    static Iterable<StackDomain> allProtocols() {
        List<StackDomain> domains = new ArrayList<>();
        domains.add(new StackDomainFakeHttp());
        domains.add(new StackDomainInMemory());
        return domains;
    }
}
