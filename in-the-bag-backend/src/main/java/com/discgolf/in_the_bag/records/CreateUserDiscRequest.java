package com.discgolf.in_the_bag.records;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.*;

public record CreateUserDiscRequest(
        @NotNull(message = "Disc ID is required")
        Long discId,

        // Optional plastic ID (could be null if using customPlastic)
        Long plasticId,

        @Size(max = 20, message = "Custom plastic must be at most 20 characters")
        String customPlastic,

        @NotBlank(message = "Color is required")
        @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Color must be a valid hex code")
        String color,

        @NotNull(message = "Weight is required")
        @Positive(message = "Weight must be positive")
        @Max(value = 999, message = "Weight must be less than 1000")
        Double weight,

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

        @Size(max = 300, message = "Comment must be at most 300 characters")
        String comment
) {}

