package com.discgolf.in_the_bag.records;

public record InventoryDiscRecord(
        Long id,
        String name,
        String type,
        Float customSpeed,
        Float customGlide,
        Float customTurn,
        Float customFade,
        String color,
        String plasticName,
        String manufacturerName,
        Float speed,
        Float glide,
        Float turn,
        Float fade,
        Boolean inUse) {
}
