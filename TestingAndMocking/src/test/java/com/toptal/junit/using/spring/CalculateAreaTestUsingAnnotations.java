package com.toptal.junit.using.spring;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*What Are Test Runners, and What Types of Runners Are There?
In the above sample, the basic runner that is used to run all the tests is BlockJUnit4ClassRunner which detects all the annotations
and run all the tests accordingly.

If we want some more functionality then we may write a custom runner. For example, in the above test class,
if we want to skip the line MockitoAnnotations.initMocks(this); then we could use a different runner that is built on top
of BlockJUnit4ClassRunner, e.g. MockitoJUnitRunner.

Using MockitoJUnitRunner, we don’t even need to initialize mocks and inject them. That will be done by MockitoJUnitRunner itself just by reading annotations.

(There’s also SpringJUnit4ClassRunner, which initializes the ApplicationContext needed for Spring integration testing—just like an
ApplicationContext is created when a Spring application starts. )*/

@RunWith(SpringJUnit4ClassRunner.class)
public class CalculateAreaTestUsingAnnotations {

    @Mock
    RectangleService rectangleService;

    //Using spy for actual call - log() method
    @Spy
    CircleService circleService;

    @InjectMocks
    CalculateArea calculateArea;

    //Not Required with @RunWith
    /*@Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
    }*/

    @Test
    public void calculateRectangleAreaTest()
    {

        Mockito.when(rectangleService.area(5.0d,4.0d)).thenReturn(20d);
        Double calculatedArea = this.calculateArea.calculateArea(Type.RECTANGLE, 5.0d, 4.0d);
        Assert.assertEquals(new Double(20d),calculatedArea);

    }

    @Test
    public void calculateCircleAreaTest()
    {

        Mockito.doCallRealMethod().when(circleService).log();
        Mockito.when(circleService.area(5.0d)).thenReturn(75d);
        Double calculatedCircleArea = this.calculateArea.calculateArea(Type.CIRCLE, 5.0d);
        circleService.log();
        Assert.assertEquals(new Double(75d),calculatedCircleArea);

    }
}