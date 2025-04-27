package com.discgolf.in_the_bag.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "Current password is required")
        String currentPassword,

        @NotBlank(message = "New password is required")
        @Size(min = 4, max = 16, message = "New password must be between 4 and 16 characters")
        String newPassword
) {}
