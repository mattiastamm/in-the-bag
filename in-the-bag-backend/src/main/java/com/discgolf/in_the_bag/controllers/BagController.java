package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.records.BagRecord;
import com.discgolf.in_the_bag.services.BagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bags")
@RequiredArgsConstructor
public class BagController {
    private final BagService bagService;

    @GetMapping("/by-userDisc/{userDiscId}")
    public List<BagRecord> getBagsByUserDiscId(@PathVariable Long userDiscId) {
        return bagService.getBagsByUserDiscId(userDiscId);
    }
}
