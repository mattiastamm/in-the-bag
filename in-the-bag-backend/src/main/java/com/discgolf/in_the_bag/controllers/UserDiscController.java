package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.*;
import com.discgolf.in_the_bag.services.UserDiscService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-discs")
public class UserDiscController {
    private final UserDiscService userDiscService;

    public UserDiscController(UserDiscService userDiscService) {
        this.userDiscService = userDiscService;
    }

    // ✅ GET ALL USER DISCS (Existing)
    @GetMapping("/{userId}")
    public List<UserDiscDto> getUserDiscs(
            @PathVariable Long userId
    ) {
        return userDiscService.getUserDiscs(userId);
    }

    // ✅ GET detailed info for specific UserDisc
    @GetMapping("/details/{userDiscId}")
    public DiscDetailsRecord getUserDiscDetails(
            @PathVariable Long userDiscId
    ) {
        return userDiscService.getDiscDetails(userDiscId);
    }

    @PostMapping
    public ResponseEntity<UserDisc> addUserDisc(@RequestBody @Valid CreateUserDiscRequest request) {
        UserDisc createdDisc = userDiscService.addDiscToUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDisc);
    }

    // ✅ PATCH request to update customizable parts for a user's disc
    @PatchMapping("/{userDiscId}")
    public ResponseEntity<DiscDetailsRecord> updateUserDisc(
            @PathVariable Long userDiscId,
            @RequestBody UpdateDiscRequest request
    ) {
        boolean isUpdated = userDiscService.updateDisc(userDiscId, request);

        if (isUpdated) {
            return ResponseEntity.noContent().build(); // ✅ 204 No Content (Success)
        } else {
            return ResponseEntity.notFound().build();  // ❌ 404 Not Found (Disc not found or wrong user)
        }
    }

    @DeleteMapping("/{userDiscId}")
    public ResponseEntity<Void> deleteUserDisc(@PathVariable Long userDiscId) {
        boolean deleted = userDiscService.deleteDisc(userDiscId);

        if (deleted) {
            return ResponseEntity.noContent().build(); // ✅ 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // ❌ 404 Not Found
        }
    }

}
