package com.testing.using.jmockit;

public class SuperClass {

    public SuperClass(final String data) {
        throw new IllegalArgumentException("Dont call me please");
    }
}
