package com.discgolf.in_the_bag.suggestions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DiscSuggestionLoader {

    // New DTO to represent the JSON structure
    public static class DiscSuggestionEntry {
        private String label;
        private List<Long> discIds;

        // Getters & Setters
        public String getLabel() {
            return label;
        }

        public List<Long> getDiscIds() {
            return discIds;
        }

    }

    private final Map<String, DiscSuggestionEntry> suggestionMap = new HashMap<>();

    @PostConstruct
    public void loadSuggestions() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getClassLoader().getResourceAsStream("suggestions/disc_suggestions.json");

            if (is == null) {
                throw new IllegalStateException("Could not find disc_suggestions.json in resources");
            }

            Map<String, DiscSuggestionEntry> loaded = mapper.readValue(is, new TypeReference<>() {});
            suggestionMap.putAll(loaded);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load disc suggestions JSON", e);
        }
    }

    public List<Long> getSuggestionsForCategory(DiscCategory category) {
        DiscSuggestionEntry entry = suggestionMap.get(category.name());
        return entry != null ? entry.getDiscIds() : List.of();
    }

    public String getLabelForCategory(DiscCategory category) {
        DiscSuggestionEntry entry = suggestionMap.get(category.name());
        return entry != null ? entry.getLabel() : "";
    }
}
