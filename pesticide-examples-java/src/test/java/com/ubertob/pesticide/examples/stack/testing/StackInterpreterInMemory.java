package com.ubertob.pesticide.examples.stack.testing;

import com.ubertob.pesticide.core.DdtProtocol;
import com.ubertob.pesticide.core.DomainOnly;
import com.ubertob.pesticide.core.DomainSetUp;
import com.ubertob.pesticide.core.Ready;
import com.ubertob.pesticide.examples.stack.MyStack;
import org.jetbrains.annotations.NotNull;

public class StackInterpreterInMemory implements StackInterpreter {

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
}
