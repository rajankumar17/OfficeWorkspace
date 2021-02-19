package com.toptal.junit.using.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public AreaController areaController() {
        return new AreaController();
    }

    @Bean
    public CalculateArea calculateArea() {
        return new CalculateArea();
    }

    @Bean
    public RectangleService rectangleService() {
        return new RectangleService();
    }

    @Bean
    public SquareService squareService() {
        return new SquareService();
    }

    @Bean
    public CircleService circleService() {
        return new CircleService();
    }
}

