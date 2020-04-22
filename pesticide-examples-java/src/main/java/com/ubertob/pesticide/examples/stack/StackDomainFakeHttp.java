package com.ubertob.pesticide.examples.stack;

import com.ubertob.pesticide.DdtProtocol;
import com.ubertob.pesticide.PureHttp;

import java.util.Stack;

public class StackDomainFakeHttp implements StackDomain {

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

    @Override
    public boolean isReady() {
        return true;
    }
}
