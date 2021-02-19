package com.toptal.junit.using.spring;

import org.springframework.stereotype.Service;

@Service
public class SquareService {

    public Double area(double r)
    {
        return r * r;
    }
}