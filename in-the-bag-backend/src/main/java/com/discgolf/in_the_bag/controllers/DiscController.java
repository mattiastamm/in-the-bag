package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.records.DiscAutoFillRecord;
import com.discgolf.in_the_bag.records.DiscSearchRecord;
import com.discgolf.in_the_bag.repositories.DiscRepository;
import com.discgolf.in_the_bag.services.DiscService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discs")
public class DiscController {

    private final DiscRepository discRepository;
    private final DiscService discService;

    @GetMapping
    public ResponseEntity<List<DiscSearchRecord>> searchDiscs(@RequestParam String name) {
        return ResponseEntity.ok(discRepository.searchDiscsByName(name));
    }

    @GetMapping("/{discId}/details")
    public ResponseEntity<DiscAutoFillRecord> getDiscDetailsForCreation(@PathVariable Long discId) {
        return discService.getDiscDetailsForCreation(discId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
