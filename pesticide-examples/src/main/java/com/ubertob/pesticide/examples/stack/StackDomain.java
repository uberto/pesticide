package com.ubertob.pesticide.examples.stack;

import com.ubertob.pesticide.DdtProtocol;
import com.ubertob.pesticide.DomainUnderTest;

interface StackDomain extends DomainUnderTest<DdtProtocol> {
    void pushNumber(int num);

    int popNumber();

    int size();
}
