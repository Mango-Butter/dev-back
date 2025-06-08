package com.mangoboss.app.api.controller;

import com.mangoboss.app.domain.service.auth.AuthService;
import com.mangoboss.app.domain.service.user.UserService;
import com.mangoboss.app.dto.auth.response.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final AuthService authService;
    private final UserService userService;

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

    @GetMapping("/test/{userId}")
    public JwtResponse getAccessToken(@PathVariable Long userId) {
        return authService.generateToken(userService.getUserById(userId));
    }

    @GetMapping("/test/error")
    public String getError() {
        throw new RuntimeException("Test error");
    }
}