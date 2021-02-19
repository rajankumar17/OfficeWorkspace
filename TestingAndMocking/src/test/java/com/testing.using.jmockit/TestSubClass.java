package com.testing.using.jmockit;

import mockit.Mocked;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestSubClass {

    private SubClass subClass;

    @Test
    public void shouldGetData(@Mocked final SuperClass superClass) {
        //Without this "@Mocked final SuperClass superClass" - java.lang.IllegalArgumentException: Dont call me please
        subClass = new SubClass("Hello");
        assertEquals("Hello", subClass.getData());
    }
}
