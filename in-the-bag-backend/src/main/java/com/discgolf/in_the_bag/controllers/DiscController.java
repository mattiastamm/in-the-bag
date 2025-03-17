package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.records.DiscAutoFillBaseRecord;
import com.discgolf.in_the_bag.records.DiscAutoFillRecord;
import com.discgolf.in_the_bag.records.DiscSearchRecord;
import com.discgolf.in_the_bag.records.PlasticRecord;
import com.discgolf.in_the_bag.repositories.DiscRepository;
import com.discgolf.in_the_bag.repositories.PlasticRepository;
import com.discgolf.in_the_bag.services.DiscService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discs")
public class DiscController {

    private final DiscRepository discRepository;
    private final DiscService discService;

    // ✅ Constructor Injection (Recommended)
    public DiscController(DiscRepository discRepository, DiscService discService) {
        this.discRepository = discRepository;
        this.discService = discService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<DiscSearchRecord>> searchDiscs(@RequestParam String query) {
        return ResponseEntity.ok(discRepository.searchDiscsByName(query));
    }

    @GetMapping("/{discId}/details")
    public ResponseEntity<DiscAutoFillRecord> getDiscDetailsForCreation(@PathVariable Long discId) {
        return discService.getDiscDetailsForCreation(discId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
