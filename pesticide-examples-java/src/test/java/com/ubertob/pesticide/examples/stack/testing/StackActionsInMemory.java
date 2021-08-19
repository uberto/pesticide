package com.ubertob.pesticide.examples.stack.testing;

import com.ubertob.pesticide.core.*;
import com.ubertob.pesticide.examples.stack.MyStack;
import org.jetbrains.annotations.NotNull;

public class StackActionsInMemory implements StackActions {

    MyStack<Integer> stack = new MyStack<Integer>();

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
        return DomainOnly.INSTANCE;
    }

    @NotNull
    @Override
    public DomainSetUp prepare() {
        return Ready.INSTANCE;
    }

    @Override
    public DdtActions<DdtProtocol> tearDown() {
        return this;
    }
}
