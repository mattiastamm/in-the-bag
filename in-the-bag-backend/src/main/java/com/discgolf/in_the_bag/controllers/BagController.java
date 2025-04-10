package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.models.Bag;
import com.discgolf.in_the_bag.records.BagRecord;
import com.discgolf.in_the_bag.records.BagWithDiscsDto;
import com.discgolf.in_the_bag.records.CreateBagRequest;
import com.discgolf.in_the_bag.records.UpdateBagDiscsRequest;
import com.discgolf.in_the_bag.services.BagService;
import com.discgolf.in_the_bag.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bags")
@RequiredArgsConstructor
public class BagController {

    private final BagService bagService;
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    @GetMapping("/by-userDisc/{userDiscId}")
    public List<BagRecord> getBagsByUserDiscId(@PathVariable Long userDiscId) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        return bagService.getBagsByUserDiscId(userId, userDiscId);
    }

    @GetMapping
    public ResponseEntity<List<BagWithDiscsDto>> getBagsWithDiscs() {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        return ResponseEntity.ok(bagService.getBagsWithDiscsForUser(userId));
    }

    @PostMapping
    public ResponseEntity<Bag> createBag(@RequestBody CreateBagRequest createBagRequest) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        Bag newBag = bagService.createBag(userId, createBagRequest.title(), createBagRequest.comment());
        return ResponseEntity.status(HttpStatus.CREATED).body(newBag);
    }


    @PutMapping("/update-discs")
    public ResponseEntity<Void> updateBagDiscs(@RequestBody UpdateBagDiscsRequest updateBagDiscsRequest) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        bagService.updateBagDiscs(userId, updateBagDiscsRequest.bagId(), updateBagDiscsRequest.userDiscIds());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove-disc")
    public ResponseEntity<Void> removeDiscFromBag(
            @RequestParam Long userDiscId,
            @RequestParam Long bagId
    ) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        bagService.removeDiscFromBag(userId, userDiscId, bagId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bagId}")
    public ResponseEntity<Void> deleteBag(@PathVariable Long bagId) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        boolean success = bagService.deleteBag(userId, bagId);

        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
