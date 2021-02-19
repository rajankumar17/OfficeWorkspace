package com.testing.using.jmockit;

import mockit.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class  ClassWithStaticMethodTest {

    @Tested
    private ClassWithStaticMethod classWithStaticMethod;

    @Injectable
    StaticValueService staticValueService;

    @Test
    public void shouldGiveStaticValue() {

        //Without this - word will have value - "My Static Value"
        new MockUp<StaticValueService>(){
            @Mock  //Mocking the static method of the class
            public String getStaticValue(){
                return "Not Static";
            }
        };
        String word = classWithStaticMethod.staticValue();
        assertEquals("Not Static", word);
    }

    //Another Way
    @Test
    public void shouldGiveStaticValue2() {
        new NonStrictExpectations(staticValueService){
            {
                StaticValueService.getStaticValue();
                returns("Not Static");
            }
        };
        String word = classWithStaticMethod.staticValue();
        assertEquals("Not Static", word);
    }

}