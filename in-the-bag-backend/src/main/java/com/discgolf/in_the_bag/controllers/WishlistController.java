package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.records.WishlistDiscDto;
import com.discgolf.in_the_bag.services.WishlistService;
import com.discgolf.in_the_bag.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    @GetMapping
    public List<WishlistDiscDto> getWishlist() {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        return wishlistService.getWishlistDiscs(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDiscsToWishlist(@RequestBody List<Long> discIds) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        wishlistService.addDiscs(userId, discIds);
        return ResponseEntity.ok("Discs added to wishlist");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromWishlist(@RequestParam Long discId) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        wishlistService.removeFromWishlist(userId, discId);
        return ResponseEntity.noContent().build();
    }
}

