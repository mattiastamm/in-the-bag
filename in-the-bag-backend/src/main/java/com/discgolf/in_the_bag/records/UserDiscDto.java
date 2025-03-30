package com.discgolf.in_the_bag.records;

public record UserDiscDto(
        Long userDiscId,
        String name,
        String type,
        Float customSpeed,
        Float customGlide,
        Float customTurn,
        Float customFade,
        String color,
        Integer plasticId,
        String plasticName,
        String customPlastic,
        String manufacturerName,
        Float speed,
        Float glide,
        Float turn,
        Float fade,
        Double weight,
        Boolean inUse,
        String comment) {
}
