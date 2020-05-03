package com.ubertob.pesticide.examples.stack;


import java.util.ArrayDeque;
import java.util.Deque;

public class MyStack<T> {

    Deque<T> deque = new ArrayDeque<>();

    public void push(T value) {
        deque.push(value);
    }

    public T pop() {
        return deque.pop();
    }

    public int size() {
        return deque.size();
    }
}
