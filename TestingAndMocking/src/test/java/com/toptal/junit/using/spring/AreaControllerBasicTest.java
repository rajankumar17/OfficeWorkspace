package com.toptal.junit.using.spring;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class AreaControllerBasicTest {

    /*
    It only tests the method call, not the actual API call.
    All those test cases where the API parameters and status of API calls need to tested for different inputs are missing.
     */
    @Mock
    CalculateArea calculateArea;

    @InjectMocks
    AreaController areaController;

    @Test
    public void calculateAreaTest() {
        Mockito.when(calculateArea.calculateArea(Type.RECTANGLE, 5.0d, 4.0d)).thenReturn(20d);

        ResponseEntity responseEntity = areaController.calculateArea("RECTANGLE", "5", "4");
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(20d, responseEntity.getBody());
    }

}