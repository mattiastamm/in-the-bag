package com.discgolf.in_the_bag.records;

public record CreateBagRequest(
        Long userId,
        String title,
        String comment // Optional
) {}

