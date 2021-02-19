package com.demo;

import com.demo.aop.DomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class SpringBootApp {

    private static final Logger LOGGER= LoggerFactory.getLogger(SpringBootApp.class);

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SpringBootApp.class, args);
        System.out.println("Spring Boot Up and Running");

       /* String[] beanNames = ctx.getBeanDefinitionNames();

        Arrays.sort(beanNames);

        for (String beanName : beanNames) {
            System.out.println(beanName);
        }*/

        LOGGER.info("Simple log statement with inputs {}, {} and {}", 1,2,3);

    }
}
