package com.discgolf.in_the_bag.records;

import java.util.List;

public record UpdateBagDiscsRequest(
        Long bagId,
        List<Long> userDiscIds
) {}
