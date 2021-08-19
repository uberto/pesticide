package com.ubertob.pesticide.examples.stack.testing;

import com.ubertob.pesticide.core.*;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class StackActionsFakeHttp implements StackActions {

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
        return new Http("local");
    }

    @NotNull
    @Override
    public DomainSetUp prepare() {
        return Ready.INSTANCE; //it should check the rest server is up
    }

    @Override
    public DdtActions<DdtProtocol> tearDown() {
        return this; //it should stop anything that it started
    }
}
