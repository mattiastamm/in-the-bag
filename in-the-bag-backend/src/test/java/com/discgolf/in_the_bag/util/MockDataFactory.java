package com.discgolf.in_the_bag.util;

import com.discgolf.in_the_bag.models.*;
import com.discgolf.in_the_bag.records.WishlistDiscDto;
import com.discgolf.in_the_bag.suggestions.DiscCategory;

import java.util.HashSet;
import java.util.Set;

public class MockDataFactory {
    public static Manufacturer createMockManufacturer() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(1);
        manufacturer.setName("Innova");
        return manufacturer;
    }

    public static Disc createMockDiscDestroyer() {
        Disc disc = new Disc();
        disc.setId(1L);
        disc.setName("Destroyer");
        disc.setManufacturer(createMockManufacturer());
        disc.setSpeed(12f);
        disc.setGlide(5f);
        disc.setTurn(-1f);
        disc.setFade(3f);
        disc.setType("Distance Driver");
        return disc;
    }

    public static Disc createMockDiscFirebird() {
        Disc disc = new Disc();
        disc.setId(1L);
        disc.setName("Firebird");
        disc.setManufacturer(createMockManufacturer());
        disc.setSpeed(9f);
        disc.setGlide(3f);
        disc.setTurn(0f);
        disc.setFade(4f);
        disc.setType("High-Speed Fairway Driver");
        return disc;
    }

    public static Plastic createMockPlastic() {
        Plastic plastic = new Plastic();
        plastic.setId(1);
        plastic.setName("Star");
        plastic.setManufacturer(createMockManufacturer());
        return plastic;
    }

    public static UserDisc createMockUserDiscDestroyer() {
        Disc disc = createMockDiscDestroyer();
        UserDisc userDisc = new UserDisc();
        userDisc.setUserDiscId(1L);
        userDisc.setUserId(1L);
        userDisc.setDisc(disc);
        userDisc.setPlastic(createMockPlastic());
        userDisc.setWeight(173d);
        userDisc.setCustomSpeed(disc.getSpeed());
        userDisc.setCustomGlide(disc.getGlide());
        userDisc.setCustomTurn(disc.getTurn());
        userDisc.setCustomFade(disc.getFade());
        userDisc.setInUse(true);
        userDisc.setUpdatedAt("2025-04-10 14:22:19");
        userDisc.setComment("Nice Disk!");
        userDisc.setColor("#7f2afe");
        return userDisc;
    }

    public static UserDisc createMockUserDiscFirebird() {
        Disc disc = createMockDiscFirebird();
        UserDisc userDisc = new UserDisc();
        userDisc.setUserDiscId(2L);
        userDisc.setUserId(1L);
        userDisc.setDisc(disc);
        userDisc.setCustomPlastic("Custom Plastic");
        userDisc.setWeight(173d);
        userDisc.setCustomSpeed(disc.getSpeed());
        userDisc.setCustomGlide(disc.getGlide());
        userDisc.setCustomTurn(disc.getTurn());
        userDisc.setCustomFade(disc.getFade());
        userDisc.setInUse(true);
        userDisc.setUpdatedAt("2025-04-10 14:22:19");
        userDisc.setComment("Nice Disk!");
        userDisc.setColor("#7f2afe");
        return userDisc;
    }


    public static Bag createMockBagWithoutDiscs() {
        Bag bag = new Bag();
        bag.setId(1L);
        bag.setUserId(1L);
        bag.setTitle("Bag Title");
        bag.setCreatedAt("2025-04-10 14:22:19");
        bag.setComment("Comment");
        bag.setUserDiscs(new HashSet<>());
        return bag;
    }

    public static Bag createMockBagWithDiscs() {
        Bag bag = new Bag();
        bag.setId(1L);
        bag.setUserId(1L);
        bag.setTitle("Bag Title");
        bag.setCreatedAt("2025-04-10 14:22:19");
        bag.setComment("Comment");
        Set<UserDisc> discs = new HashSet<>();
        discs.add(createMockUserDiscDestroyer());
        discs.add(createMockUserDiscFirebird());
        bag.setUserDiscs(discs);
        return bag;
    }

    public static WishlistDiscDto createMockWishlistDiscDto() {
        return new WishlistDiscDto(
                1L, // discId
                10L, // suggestionId
                "Destroyer",
                "Innova",
                12f, 5f, -1f, 3f,
                "Distance Driver",
                "Stable"
        );
    }

    public static Suggestion createMockSuggestion(Long id) {
        Suggestion suggestion = new Suggestion();
        suggestion.setId(id);
        suggestion.setDisc(createMockDiscDestroyer());  // Reuse your existing mock Disc
        suggestion.setDiscCategory(DiscCategory.STABLE_DRIVER);  // Enum
        suggestion.setCategory("Distance Driver");
        suggestion.setStability("Stable");
        return suggestion;
    }


}
