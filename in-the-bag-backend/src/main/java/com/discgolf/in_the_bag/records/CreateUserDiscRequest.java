package com.discgolf.in_the_bag.records;

import jakarta.validation.constraints.NotNull;

public record CreateUserDiscRequest(
        @NotNull Long userId,
        @NotNull Long discId,
        @NotNull Long plasticId,
        @NotNull String color,
        @NotNull Double weight,
        @NotNull Float customSpeed,
        @NotNull Float customGlide,
        @NotNull Float customTurn,
        @NotNull Float customFade,
        String comment
) {}
