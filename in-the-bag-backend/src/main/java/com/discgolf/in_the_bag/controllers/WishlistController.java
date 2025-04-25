package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.records.WishlistAddRequest;
import com.discgolf.in_the_bag.records.WishlistDiscDto;
import com.discgolf.in_the_bag.services.WishlistService;
import com.discgolf.in_the_bag.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    @GetMapping
    public List<WishlistDiscDto> getWishlist() {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        return wishlistService.getWishlistDiscs(userId);
    }

    @PostMapping
    public ResponseEntity<Void> addToWishlist(@RequestBody WishlistAddRequest wishlistAddRequest) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        wishlistService.addToWishlist(userId, wishlistAddRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{suggestionId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long suggestionId) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        wishlistService.removeFromWishlist(userId, suggestionId);
        return ResponseEntity.noContent().build();
    }
}

