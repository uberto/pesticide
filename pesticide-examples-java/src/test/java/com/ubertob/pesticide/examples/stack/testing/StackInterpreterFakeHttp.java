package com.ubertob.pesticide.examples.stack.testing;

import com.ubertob.pesticide.DdtProtocol;
import com.ubertob.pesticide.DomainSetUp;
import com.ubertob.pesticide.PureHttp;
import com.ubertob.pesticide.Ready;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class StackInterpreterFakeHttp implements StackInterpreter {

    Stack<Integer> stack = new Stack<>(); //a call to a http server

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
        return new PureHttp("local");
    }

    @NotNull
    @Override
    public DomainSetUp prepare() {
        return Ready.INSTANCE; //it should check the rest server is up
    }
}
