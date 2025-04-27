package com.discgolf.in_the_bag.records;

import jakarta.validation.constraints.*;

public record UpdateDiscRequest(
        @NotNull(message = "Speed is required")
        @DecimalMin(value = "1.0", message = "Speed must be at least 1")
        @DecimalMax(value = "14.0", message = "Speed must be at most 14")
        Float customSpeed,

        @NotNull(message = "Glide is required")
        @DecimalMin(value = "1.0", message = "Glide must be at least 1")
        @DecimalMax(value = "7.0", message = "Glide must be at most 7")
        Float customGlide,

        @NotNull(message = "Turn is required")
        @DecimalMin(value = "-5.0", message = "Turn must be at least -5")
        @DecimalMax(value = "1.0", message = "Turn must be at most 1")
        Float customTurn,

        @NotNull(message = "Fade is required")
        @DecimalMin(value = "0.0", message = "Fade must be at least 0")
        @DecimalMax(value = "5.0", message = "Fade must be at most 5")
        Float customFade,

        @NotNull(message = "Color is required")
        @Pattern(
                regexp = "^#([A-Fa-f0-9]{6})$",
                message = "Color must be a valid HEX code (e.g., #FFFFFF)"
        )
        String color,

        Long plasticId, // Optional, no validation needed

        @Size(max = 20, message = "Custom plastic name must be 20 characters or less")
        String customPlastic,

        @DecimalMin(value = "0.1", message = "Weight must be positive")
        @DecimalMax(value = "999.9", message = "Weight must be less than 1000")
        Double weight,

        String comment // Optional, no validation needed
) {}

