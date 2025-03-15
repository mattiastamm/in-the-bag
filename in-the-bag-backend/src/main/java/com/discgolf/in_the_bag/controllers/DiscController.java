package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.DiscDetailsRecord;
import com.discgolf.in_the_bag.records.InventoryDiscRecord;
import com.discgolf.in_the_bag.records.UpdateDiscRequest;
import com.discgolf.in_the_bag.services.DiscService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discs")
public class DiscController {
    private final DiscService discService;

    public DiscController(DiscService discService) {
        this.discService = discService;
    }

    // ✅ GET ALL USER DISCS (Existing)
    @GetMapping
    public List<InventoryDiscRecord> getUserDiscs() {
        Long userId = 1L; // Hardcoded for now
        return discService.getUserDiscs(userId);
    }

    // ✅ GET detailed info for specific UserDisc
    @GetMapping("/{userId}/{discId}")
    public DiscDetailsRecord getDiscDetails(
            @PathVariable Long userId,
            @PathVariable Long discId
    ) {
        return discService.getDiscDetails(userId, discId);
    }

    // ✅ PATCH request to update customizable parts for a user's disc
    @PatchMapping("/{userId}/{discId}")
    public ResponseEntity<DiscDetailsRecord> updateDisc(
            @PathVariable Long userId, // ✅ Now requires userId
            @PathVariable Long discId,
            @RequestBody UpdateDiscRequest request
    ) {
        boolean isUpdated = discService.updateDisc(userId, discId, request);

        if (isUpdated) {
            return ResponseEntity.noContent().build(); // ✅ 204 No Content (Success)
        } else {
            return ResponseEntity.notFound().build();  // ❌ 404 Not Found (Disc not found or wrong user)
        }
    }

}
