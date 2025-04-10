package com.discgolf.in_the_bag.records;

public record UserProfileDto(
        String email,
        String createdAt,
        int totalDiscs,
        int totalBags,
        int discsInUse,
        int putters,
        int midranges,
        int fairways,
        int drivers
) {}
