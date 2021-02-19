package com.testing.using.jmockit;

public class ClassWithPrivateMethod {

    private int sum(int i, int j) {
        return i + j;
    }

    public int publicCallsPrivate(int i, int j){
        return sum(i,j);
    }
}
