package com.discgolf.in_the_bag.suggestions;

public record DiscSuggestionDto(
        Long id,
        String name,
        String manufacturer,
        float speed,
        float glide,
        float turn,
        float fade
) {}
