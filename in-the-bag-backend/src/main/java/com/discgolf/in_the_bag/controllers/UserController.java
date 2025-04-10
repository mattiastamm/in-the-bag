package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.records.ChangePasswordRequest;
import com.discgolf.in_the_bag.records.DeleteAccountRequest;
import com.discgolf.in_the_bag.records.UserProfileDto;
import com.discgolf.in_the_bag.services.UserService;
import com.discgolf.in_the_bag.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        userService.changePassword(userId, changePasswordRequest);
        return ResponseEntity.ok("Password updated successfully");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@RequestBody DeleteAccountRequest deleteAccountRequest) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        userService.deleteAccount(userId, deleteAccountRequest.password());
        return ResponseEntity.ok("Account deleted successfully.");
    }

}
