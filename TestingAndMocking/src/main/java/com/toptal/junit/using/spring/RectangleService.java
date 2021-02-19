package com.toptal.junit.using.spring;

import org.springframework.stereotype.Service;

@Service
public class RectangleService {

    public Double area(Double r, Double h)
    {
        return r * h;
    }

}
