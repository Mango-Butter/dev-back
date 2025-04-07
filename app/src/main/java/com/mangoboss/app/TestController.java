package com.mangoboss.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Value("${test_env}")
    private String test_env;

    @GetMapping
    public String getHello() {
        return "hello";
    }

    @GetMapping("/test")
    public String getTest() {
        return test_env;
    }
}