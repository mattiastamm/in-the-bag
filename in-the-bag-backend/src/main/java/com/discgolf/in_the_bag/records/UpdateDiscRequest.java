package com.discgolf.in_the_bag.records;

public record UpdateDiscRequest(
        Float customSpeed,
        Float customGlide,
        Float customTurn,
        Float customFade,
        String color,
        Long plasticId,
        Double weight,
        String comment
) {}
