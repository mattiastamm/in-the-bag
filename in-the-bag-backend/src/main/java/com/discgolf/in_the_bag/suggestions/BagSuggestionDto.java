package com.discgolf.in_the_bag.suggestions;

import com.discgolf.in_the_bag.models.Suggestion;

import java.util.List;

public record BagSuggestionDto(String categoryTitle, List<DiscSuggestionDto> discSuggestionDtos) {
}
