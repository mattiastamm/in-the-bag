package com.discgolf.in_the_bag.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @Email(message = "Must be a valid email")
        @NotBlank(message = "Email is required")
        @Size(max = 100, message = "Email must be at most 100 characters")
        String email,
        @NotBlank(message = "Password is required")
        @Size(min = 4, max = 16, message = "Password must be between 4 and 16 characters")
        String password
) {}
