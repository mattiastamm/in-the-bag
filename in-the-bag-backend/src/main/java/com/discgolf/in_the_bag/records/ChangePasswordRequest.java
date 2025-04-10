package com.discgolf.in_the_bag.records;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {}
