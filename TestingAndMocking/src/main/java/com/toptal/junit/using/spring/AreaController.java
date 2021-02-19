package com.toptal.junit.using.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AreaController {

    @Autowired
    CalculateArea calculateArea;

    @RequestMapping(value = "api/area", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity calculateArea(
            @RequestParam("type") String type,
            @RequestParam("param1") String param1,
            @RequestParam(value = "param2", required = false) String param2
    ) {
        try {
            Double area = calculateArea.calculateArea(
                    Type.valueOf(type),
                    Double.parseDouble(param1),
                    Double.parseDouble(param2)
            );
            return new ResponseEntity(area, HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity(e.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}