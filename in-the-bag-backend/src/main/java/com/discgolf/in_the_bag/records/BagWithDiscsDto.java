package com.discgolf.in_the_bag.records;

import java.util.List;

public record BagWithDiscsDto(
        Long id,
        String title,
        String comment,
        String createdAt,
        List<UserDiscDto> discs
) {}

