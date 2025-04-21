package com.discgolf.in_the_bag.records;

public record WishlistDiscDto(
        Long id,
        String name,
        String manufacturer,
        Float speed,
        Float glide,
        Float turn,
        Float fade,
        String type
) {}
