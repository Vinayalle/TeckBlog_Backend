package com.backend.techblogs.controller;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {

    @Value("${spring.datasource.url}")
    private String dburl;


    @GetMapping
    public String getDBUrl(){
         return dburl;
    }
}
