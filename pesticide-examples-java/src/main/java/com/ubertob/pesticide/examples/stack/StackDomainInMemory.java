package com.ubertob.pesticide.examples.stack;

import com.ubertob.pesticide.DdtProtocol;
import com.ubertob.pesticide.DomainSetUp;
import com.ubertob.pesticide.InMemoryHubs;
import com.ubertob.pesticide.Ready;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class StackDomainInMemory implements StackDomain {

    Stack<Integer> stack = new Stack<Integer>();

    @Override
    public void pushNumber(int num) {
        stack.push(num);
    }

    @Override
    public int popNumber() {
        return stack.pop();
    }

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public DdtProtocol getProtocol() {
        return InMemoryHubs.INSTANCE;
    }

    @NotNull
    @Override
    public DomainSetUp prepare() {
        return Ready.INSTANCE;
    }
}
