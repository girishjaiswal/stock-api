package com.assignment.stockapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication(scanBasePackages = {"com.assignment.stockapi.controller", "com.assignment.stockapi.service", "com.assignment.stockapi.entity", "com.assignment.stockapi.repository"})
@ComponentScan("com.assignment.stockapi")
public class StockApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockApiApplication.class, args);
    }
}
