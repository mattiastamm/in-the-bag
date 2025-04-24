package com.discgolf.in_the_bag.records;

public record WishlistDiscDto(
        Long discId,
        Long suggestionId,
        String name,
        String manufacturer,
        Float speed,
        Float glide,
        Float turn,
        Float fade,
        String category,
        String stability
) {}
