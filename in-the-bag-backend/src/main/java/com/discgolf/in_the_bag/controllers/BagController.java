package com.discgolf.in_the_bag.controllers;

import com.discgolf.in_the_bag.models.Bag;
import com.discgolf.in_the_bag.records.BagRecord;
import com.discgolf.in_the_bag.records.BagWithDiscsDto;
import com.discgolf.in_the_bag.records.CreateBagRequest;
import com.discgolf.in_the_bag.records.UpdateBagDiscsRequest;
import com.discgolf.in_the_bag.services.BagService;
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

    @GetMapping("/by-userDisc/{userDiscId}")
    public List<BagRecord> getBagsByUserDiscId(@PathVariable Long userDiscId) {
        return bagService.getBagsByUserDiscId(userDiscId);
    }

    @GetMapping("/{userId}/bags-with-discs")
    public ResponseEntity<List<BagWithDiscsDto>> getBagsWithDiscs(@PathVariable Long userId) {
        return ResponseEntity.ok(bagService.getBagsWithDiscsForUser(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<Bag> createBag(@RequestBody CreateBagRequest request) {
        Bag newBag = bagService.createBag(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBag);
    }


    @PutMapping("/update-discs")
    public ResponseEntity<Void> updateBagDiscs(@RequestBody UpdateBagDiscsRequest request) {
        bagService.updateBagDiscs(request.bagId(), request.userDiscIds());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove-disc")
    public ResponseEntity<Void> removeDiscFromBag(
            @RequestParam Long userDiscId,
            @RequestParam Long bagId
    ) {
        bagService.removeDiscFromBag(userDiscId, bagId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBag(@RequestParam Long bagId) {
        boolean success = bagService.deleteBag(bagId);

        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
