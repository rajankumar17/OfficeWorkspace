package com.toptal.junit.using.spring;

import org.springframework.stereotype.Service;

@Service
public class CircleService {

    public Double area(Double r)
    {
        return Math.PI * r * r;
    }

    public void log() {
        System.out.println("Using spy to print the log msg by calling actual method");
    }
}