package com.bing.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloWorldController {
    @Value("${server.port}")
    int a;

    @RequestMapping("/test")
    public String test(){
        return a+"";
    }


}
