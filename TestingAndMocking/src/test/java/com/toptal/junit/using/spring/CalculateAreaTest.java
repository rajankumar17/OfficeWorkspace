package com.toptal.junit.using.spring;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CalculateAreaTest {

    RectangleService rectangleService;
    SquareService squareService;
    CircleService circleService;

    CalculateArea calculateArea;

    @Before
    public void init()
    {
        rectangleService = Mockito.mock(RectangleService.class);
        squareService = Mockito.mock(SquareService.class);
        circleService = Mockito.mock(CircleService.class);
        calculateArea = new CalculateArea(squareService,rectangleService,circleService);
    }

    @Test
    public void calculateRectangleAreaTest()
    {

        Mockito.when(rectangleService.area(5.0d,4.0d)).thenReturn(20d);
        Double calculatedArea = this.calculateArea.calculateArea(Type.RECTANGLE, 5.0d, 4.0d);
        Assert.assertEquals(new Double(20d),calculatedArea);

    }
}