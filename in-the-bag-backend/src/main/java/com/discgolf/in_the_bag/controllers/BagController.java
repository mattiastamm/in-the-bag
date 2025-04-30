package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.models.Bag;
import com.discgolf.in_the_bag.records.BagWithDiscsDto;
import com.discgolf.in_the_bag.records.CreateBagRequest;
import com.discgolf.in_the_bag.records.UpdateBagDiscsRequest;
import com.discgolf.in_the_bag.services.BagService;
import com.discgolf.in_the_bag.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bags")
@RequiredArgsConstructor
public class BagController {

    private final BagService bagService;
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<BagWithDiscsDto>> getBagsWithDiscs() {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        return ResponseEntity.ok(bagService.getBagsWithDiscsForUser(userId));
    }

    @PostMapping
    public ResponseEntity<Bag> createBag(@RequestBody @Valid CreateBagRequest createBagRequest) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        Bag newBag = bagService.createBag(userId, createBagRequest.title(), createBagRequest.comment());
        return ResponseEntity.status(HttpStatus.CREATED).body(newBag);
    }


    @PutMapping("/{bagId}")
    public ResponseEntity<Void> updateBagDiscs(
            @PathVariable Long bagId,
            @RequestBody UpdateBagDiscsRequest updateBagDiscsRequest
    ) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        bagService.updateBagDiscs(userId, bagId, updateBagDiscsRequest.userDiscIds());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bagId}/discs/{userDiscId}")
    public ResponseEntity<Void> removeDiscFromBag(
            @PathVariable Long bagId,
            @PathVariable Long userDiscId
    ) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        bagService.removeDiscFromBag(userId, userDiscId, bagId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bagId}")
    public ResponseEntity<Void> deleteBag(@PathVariable Long bagId) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        bagService.deleteBag(userId, bagId);
        return ResponseEntity.ok().build();
    }

}
