package com.discgolf.in_the_bag.records;

import java.util.List;

public record DiscAutoFillRecord(
        Long id,
        String name,
        String type,
        String manufacturerName,
        Float speed,
        Float glide,
        Float turn,
        Float fade,
        List<PlasticRecord> availablePlastics
) {}
