package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.*;
import com.discgolf.in_the_bag.services.UserDiscService;
import com.discgolf.in_the_bag.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-discs")
public class UserDiscController {
    private final UserDiscService userDiscService;
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    // ✅ GET ALL USER DISCS (Existing)
    @GetMapping
    public List<UserDiscDto> getUserDiscs() {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        return userDiscService.getUserDiscs(userId);
    }

    // ✅ GET detailed info for specific UserDisc
    @GetMapping("/{userDiscId}/details")
    public DiscDetailsRecord getUserDiscDetails( @PathVariable Long userDiscId ) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        return userDiscService.getDiscDetails(userId, userDiscId);
    }

    @PostMapping
    public ResponseEntity<UserDisc> addUserDisc(@RequestBody @Valid CreateUserDiscRequest createUserDiscRequest) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        UserDisc createdDisc = userDiscService.addDiscToUser(userId, createUserDiscRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDisc);
    }

    // ✅ PATCH request to update customizable parts of a user's disc
    @PatchMapping("/{userDiscId}")
    public ResponseEntity<DiscDetailsRecord> updateUserDisc(
            @PathVariable Long userDiscId,
            @RequestBody @Valid UpdateDiscRequest updateDiscRequest
    ) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        boolean isUpdated = userDiscService.updateDisc(userId, userDiscId, updateDiscRequest);

        if (isUpdated) {
            return ResponseEntity.noContent().build(); // ✅ 204 No Content (Success)
        } else {
            return ResponseEntity.notFound().build();  // ❌ 404 Not Found (Disc not found or wrong user)
        }
    }

    @DeleteMapping("/{userDiscId}")
    public ResponseEntity<Void> deleteUserDisc(@PathVariable Long userDiscId) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        boolean deleted = userDiscService.deleteDisc(userId, userDiscId);

        if (deleted) {
            return ResponseEntity.noContent().build(); // ✅ 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // ❌ 404 Not Found
        }
    }

}
