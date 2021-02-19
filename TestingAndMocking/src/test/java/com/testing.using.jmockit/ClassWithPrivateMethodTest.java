package com.testing.using.jmockit;

import mockit.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClassWithPrivateMethodTest {

    @Tested
    private ClassWithPrivateMethod classWithPrivateMethod;

    //Invoke actual Private Method
    @Test
    public void shouldGetSum() {
        int sum = Deencapsulation.invoke(classWithPrivateMethod, "sum", 1, 2);
        assertEquals(3, sum);
    }

    //Another way - Invoke Private Method but expect our defined value as per expectation
    @Test
    public void shouldGetSum2() {
        new Expectations(classWithPrivateMethod){{
            Deencapsulation.invoke(classWithPrivateMethod,"sum", 1, 2);
            returns(5);
        }};

        classWithPrivateMethod.publicCallsPrivate(1,2);
        assertEquals(5, 5);
    }

    //Another way -MockUp way
    @Test
    public void shouldGetSum3() {
        new MockUp<ClassWithPrivateMethod>(){
            //Override the private method
            //Dont provide any ACCESSS MODIFIER!
            @Mock
            int sum(int i,int j){
                return 5;
            }
        };

        classWithPrivateMethod.publicCallsPrivate(1,2);
        assertEquals(5, 5);
    }

}