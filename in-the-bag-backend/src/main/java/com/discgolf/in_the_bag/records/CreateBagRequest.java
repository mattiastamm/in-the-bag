package com.discgolf.in_the_bag.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBagRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 15, message = "Title must be at most 15 characters")
        String title,

        @Size(max = 200, message = "Comment must be at most 200 characters")
        String comment // Still optional, just limited in size
) {}

