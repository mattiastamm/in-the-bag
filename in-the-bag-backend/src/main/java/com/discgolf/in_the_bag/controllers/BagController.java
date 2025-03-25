package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.records.BagRecord;
import com.discgolf.in_the_bag.records.BagWithDiscsDto;
import com.discgolf.in_the_bag.services.BagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bags")
@RequiredArgsConstructor
public class BagController {
    private final BagService bagService;

    @GetMapping("/by-userDisc/{userDiscId}")
    public List<BagRecord> getBagsByUserDiscId(@PathVariable Long userDiscId) {
        return bagService.getBagsByUserDiscId(userDiscId);
    }

    @GetMapping("/{userId}/bags-with-discs")
    public ResponseEntity<List<BagWithDiscsDto>> getBagsWithDiscs(@PathVariable Long userId) {
        return ResponseEntity.ok(bagService.getBagsWithDiscsForUser(userId));
    }

    @DeleteMapping("/remove-disc")
    public ResponseEntity<Void> removeDiscFromBag(
            @RequestParam Long userDiscId,
            @RequestParam Long bagId
    ) {
        bagService.removeDiscFromBag(userDiscId, bagId);
        return ResponseEntity.noContent().build();
    }

}
