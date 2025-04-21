package com.discgolf.in_the_bag.suggestions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DiscSuggestionLoader {

    private static final Logger logger = LoggerFactory.getLogger(DiscSuggestionLoader.class);

    private final Map<String, DiscSuggestionJsonFormatDto> suggestions = new HashMap<>();

    public Map<String, DiscSuggestionJsonFormatDto> getSuggestions() {
        return Collections.unmodifiableMap(suggestions);
    }

    @PostConstruct
    public void loadSuggestions() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClassPathResource resource = new ClassPathResource("disc_suggestions.json");

            Map<String, DiscSuggestionJsonFormatDto> loadedSuggestions = objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<>() {}
            );

            suggestions.clear();
            suggestions.putAll(loadedSuggestions);

            logger.info("Loaded {} suggestion categories from JSON", suggestions.size());

        } catch (IOException e) {
            logger.error("Failed to load suggestions from disc_suggestions.json", e);
        }
    }

    public List<Long> getSuggestionsForCategory(DiscCategory category) {
        DiscSuggestionJsonFormatDto entry = suggestions.get(category.name());
        return entry != null ? entry.discIds() : List.of();
    }

    public String getLabelForCategory(DiscCategory category) {
        DiscSuggestionJsonFormatDto entry = suggestions.get(category.name());
        return entry != null ? entry.label() : "";
    }

    public Set<Long> getAllSuggestionDiscIds() {
        return suggestions.values().stream()
                .flatMap(entry -> entry.discIds().stream())
                .collect(Collectors.toSet());
    }

}
