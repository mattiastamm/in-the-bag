package com.discgolf.in_the_bag.suggestions;

import com.discgolf.in_the_bag.models.UserDisc;

public record BagSuggestionInputDto(Long userDiscId, String type, Float speed, Float glide, Float turn, Float fade) {
    public static BagSuggestionInputDto from(UserDisc disc) {
        return new BagSuggestionInputDto(
                disc.getId(),
                disc.getDisc().getType(),
                disc.getCustomSpeed(),
                disc.getCustomGlide(),
                disc.getCustomTurn(),
                disc.getCustomFade()
        );
    }
}


