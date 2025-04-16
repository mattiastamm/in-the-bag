package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.records.LoginRequest;
import com.discgolf.in_the_bag.records.LoginResponse;
import com.discgolf.in_the_bag.records.SignupRequest;
import com.discgolf.in_the_bag.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.authenticateUser(request);
    }

    @PostMapping("/signup")
    public LoginResponse signup(@RequestBody SignupRequest request) {
        return userService.signup(request);
    }
}
