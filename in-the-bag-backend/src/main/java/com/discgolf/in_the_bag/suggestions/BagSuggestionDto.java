package com.discgolf.in_the_bag.suggestions;

import java.util.List;

public record BagSuggestionDto(String categoryLabel, List<Long> disc_ids) {
}
