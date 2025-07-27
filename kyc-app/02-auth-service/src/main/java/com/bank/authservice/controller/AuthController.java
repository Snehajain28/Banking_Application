package com.bank.authservice.controller;

import com.bank.authservice.dto.LoginRequest;
import com.bank.authservice.dto.RegisterRequest;
import com.bank.authservice.model.User;
import com.bank.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
