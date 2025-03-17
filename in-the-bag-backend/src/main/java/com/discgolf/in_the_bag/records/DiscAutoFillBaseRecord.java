package com.discgolf.in_the_bag.records;


public record DiscAutoFillBaseRecord(
        Long id,
        String name,
        String type,
        Integer manufacturerId,
        String manufacturerName,
        Float speed,
        Float glide,
        Float turn,
        Float fade
) {}
