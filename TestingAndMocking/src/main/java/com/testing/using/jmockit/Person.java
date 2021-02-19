package com.testing.using.jmockit;

public class Person {

    private Allocator allocator;
    private String personName;
    static int balanceAmount;

    public Person(){
        personName = "Default Rajan";
    }

    public Person(String personName){
        this.personName = personName;
    }

    Person(Allocator allocator) {
        this.allocator = allocator;
    }

    public String getDetails(String name) {
        return allocator.getAllocation(name);
    }

    public String setName(String name) {
        if (NameValidator.isNameValid(name)) {
            return name;
        } else {
            return "DEFAULT";
        }
    }

    public String getAllocationValue() {
        return allocator.getAllocationValue();
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    //Static block begins
    static {
        updateBalance(100);
    }

    public static void updateBalance(float balance) {
        balanceAmount += balance;
    }
}
