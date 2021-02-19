package com.testing.using.jmockit;

public class Allocator {
    String allocationValue;

    Allocator(String value) {
        this.allocationValue = value;
    }

    public String getAllocationValue() {
        return this.allocationValue;
    }

    public String getAllocation(String name) {
        if (name.equalsIgnoreCase("knolder")) {
            return "Welcome to Knoldus";
        } else {
            return "Not allocated";
        }
    }
}
