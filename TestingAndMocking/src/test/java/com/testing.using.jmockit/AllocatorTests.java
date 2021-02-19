package com.testing.using.jmockit;

import mockit.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AllocatorTests {

    @Tested
    Person person;

    @Injectable
    Allocator allocator;

    //we will use MockUp to mock static method of NameValidator such that even after giving an invalid value we will get a desired value
    @Test
    public void setPersonShouldReturnValidNameWithMocking() {
        new MockUp <NameValidator>() {
            @Mock
            Boolean isNameValid(String name) {
                return true;
            }
        };
        String actualResult = person.setName("123");
        String expectedValidResult = "123";
        assertEquals("expected value does not match to the actual value",
                expectedValidResult, actualResult);

        assertEquals("expected value does not match to the actual value",
                true, NameValidator.isNameValid("Rajan"));
    }

    //we will use Expectations to mock a method of Allocator such that even after giving a valid value we will get the message we provide
    @Test
    public void getDetailsShouldReturnNotAllocatedMessage() {
        new Expectations() {
            {
                allocator.getAllocation("knolder");
                result = "Not allocated";
                maxTimes = 1; //Here instead of maxTimes, we can use minTimes and times depending of our logical scenario.
            }
        };
        assertEquals("expected value does not match to the actual value",
                "Not allocated", person.getDetails("knolder"));
    }

    //we wanted to use a particular instance of Allocator with a particular allocationValue,
    // but since it is a private member, we can’t use Expectations or MockUp, So we moved to Deencapsulation for help.
    @Test
    public void getAllocationShouldReturnDefaultValue() {
        Deencapsulation.setField(person, "allocator", new Allocator("nothing"));
        assertEquals("value not matched", "nothing", person.getAllocationValue());
    }

    //Mock Constructor
    @Test
    public void testGetName() {
        new MockUp<Person>() {
            @Mock
            public void $init() {
                //Dont assign name variable at all
                //Leave it null
            }

        };

        new MockUp<Person>() {
            @Mock
            public void $init(String personName) {
                //Dont assign name variable at all
                //Leave it null
            }

        };

        Person p = new Person();
        String name = p.getPersonName();
        assertNull("Name of person is null",name);

        Person p2 = new Person("Hello Rajan");
        String name2 = p2.getPersonName();
        assertNull("Name of person is null",name2);
    }

    @Test
    public void testPersonStaticBlock(){

        new MockUp<Person>(){
            @Mock
            public void $clinit(){
                Person.updateBalance(500);
            }
        };
        //"Static class initializers (including assignments to static fields) of a mocked class are not affected, unless specified otherwise."
        //So this assert will fail since static block already has initialized the value of the static variable balanceAmount
        //assertEquals("The balance amount is 500", 500, Person.balanceAmount);

    }

}
