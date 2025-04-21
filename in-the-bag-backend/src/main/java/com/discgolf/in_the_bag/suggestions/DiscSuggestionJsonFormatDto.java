package com.discgolf.in_the_bag.suggestions;

import java.util.List;

public record DiscSuggestionJsonFormatDto (
        String label,
        List<Long> discIds
) {}

