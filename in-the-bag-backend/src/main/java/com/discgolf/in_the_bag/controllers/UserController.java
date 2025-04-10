package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.records.UserProfileDto;
import com.discgolf.in_the_bag.services.UserService;
import com.discgolf.in_the_bag.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getOwnProfile() {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }
}
