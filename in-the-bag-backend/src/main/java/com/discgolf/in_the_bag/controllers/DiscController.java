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
    @GetMapping("/{userId}")
    public List<InventoryDiscRecord> getUserDiscs(
            @PathVariable Long userId
    ) {
        return discService.getUserDiscs(userId);
    }

    // ✅ GET detailed info for specific UserDisc
    @GetMapping("/details/{userDiscId}")
    public DiscDetailsRecord getDiscDetails(
            @PathVariable Long userDiscId
    ) {
        return discService.getDiscDetails(userDiscId);
    }

    // ✅ PATCH request to update customizable parts for a user's disc
    @PatchMapping("/{userDiscId}")
    public ResponseEntity<DiscDetailsRecord> updateDisc(
            @PathVariable Long userDiscId,
            @RequestBody UpdateDiscRequest request
    ) {
        boolean isUpdated = discService.updateDisc(userDiscId, request);

        if (isUpdated) {
            return ResponseEntity.noContent().build(); // ✅ 204 No Content (Success)
        } else {
            return ResponseEntity.notFound().build();  // ❌ 404 Not Found (Disc not found or wrong user)
        }
    }

    @DeleteMapping("/{userDiscId}")
    public ResponseEntity<Void> deleteDisc(@PathVariable Long userDiscId) {
        boolean deleted = discService.deleteDisc(userDiscId);

        if (deleted) {
            return ResponseEntity.noContent().build(); // ✅ 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // ❌ 404 Not Found
        }
    }

}
