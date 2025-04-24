package com.discgolf.in_the_bag.suggestions;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public enum DiscCategory {
    UNDERSTABLE_PUTT_APPROACH,
    UNDERSTABLE_MIDRANGE,
    UNDERSTABLE_FAIRWAY_6,
    UNDERSTABLE_FAIRWAY_7,
    UNDERSTABLE_FAIRWAY_8,
    UNDERSTABLE_FAIRWAY_9,
    UNDERSTABLE_DRIVER,

    STABLE_PUTT_APPROACH,
    STABLE_MIDRANGE,
    STABLE_FAIRWAY_6,
    STABLE_FAIRWAY_7,
    STABLE_FAIRWAY_8,
    STABLE_FAIRWAY_9,
    STABLE_DRIVER,

    OVERSTABLE_APPROACH,
    OVERSTABLE_MIDRANGE,
    OVERSTABLE_FAIRWAY_6,
    OVERSTABLE_FAIRWAY_7,
    OVERSTABLE_FAIRWAY_8,
    OVERSTABLE_FAIRWAY_9,
    OVERSTABLE_DRIVER;


    public static final Map<DiscCategory, String> CATEGORY_TITLES;

    static {
        Map<DiscCategory, String> titles = new EnumMap<>(DiscCategory.class);
        titles.put(UNDERSTABLE_FAIRWAY_7, "Understable low-speed Fairway Driver");
        titles.put(UNDERSTABLE_FAIRWAY_9, "Understable high-speed Fairway Driver");
        titles.put(UNDERSTABLE_DRIVER, "Understable Distance Driver");

        titles.put(STABLE_PUTT_APPROACH, "Stable Putt & Approach");
        titles.put(STABLE_MIDRANGE, "Stable Midrange");
        titles.put(STABLE_FAIRWAY_7, "Stable low-speed Fairway Driver");
        titles.put(STABLE_FAIRWAY_9, "Stable high-speed Fairway Driver");
        titles.put(STABLE_DRIVER, "Stable Distance Driver");

        titles.put(OVERSTABLE_APPROACH, "Overstable Approach");
        titles.put(OVERSTABLE_MIDRANGE, "Overstable Midrange");
        titles.put(OVERSTABLE_FAIRWAY_7, "Overstable low-speed Fairway Driver");
        titles.put(OVERSTABLE_FAIRWAY_9, "Overstable high-speed Fairway Driver");
        titles.put(OVERSTABLE_DRIVER, "Overstable Distance Driver");

        CATEGORY_TITLES = Collections.unmodifiableMap(titles);
    }

    public String getTitle() {
        return CATEGORY_TITLES.get(this);
    }

    // Order of disc categories to check for when there are no special cases
    public static final List<DiscCategory> CHECK_ORDER_SIMPLE = List.of(
            STABLE_PUTT_APPROACH, STABLE_MIDRANGE, UNDERSTABLE_FAIRWAY_7, OVERSTABLE_MIDRANGE, STABLE_FAIRWAY_7,
            OVERSTABLE_APPROACH, UNDERSTABLE_FAIRWAY_9, STABLE_FAIRWAY_9, UNDERSTABLE_DRIVER, OVERSTABLE_FAIRWAY_7,
            OVERSTABLE_FAIRWAY_9, STABLE_DRIVER, OVERSTABLE_DRIVER
    );

    // Order of disc categories to check for when there is an Understable Putt & Approach disc in the bag (we don't suggest Stable Putt & Approach)
    public static final List<DiscCategory> CHECK_ORDER_FOR_UNDERSTABLE_PUTT_APPROACH = List.of(
            STABLE_MIDRANGE, UNDERSTABLE_FAIRWAY_7, OVERSTABLE_MIDRANGE, STABLE_FAIRWAY_7,
            OVERSTABLE_APPROACH, UNDERSTABLE_FAIRWAY_9, STABLE_FAIRWAY_9, UNDERSTABLE_DRIVER, OVERSTABLE_FAIRWAY_7,
            OVERSTABLE_FAIRWAY_9, STABLE_DRIVER, OVERSTABLE_DRIVER
    );

    // Order of disc categories to check for when there is an Understable Midrange in the bag (Stable Midrange moves up to 6th place in the order)
    public static final List<DiscCategory> CHECK_ORDER_FOR_UNDERSTABLE_MIDRANGE = List.of(
            STABLE_PUTT_APPROACH, UNDERSTABLE_FAIRWAY_7, OVERSTABLE_MIDRANGE, STABLE_FAIRWAY_7,
            OVERSTABLE_APPROACH, STABLE_MIDRANGE, UNDERSTABLE_FAIRWAY_9, STABLE_FAIRWAY_9, UNDERSTABLE_DRIVER,
            OVERSTABLE_FAIRWAY_7, OVERSTABLE_FAIRWAY_9, STABLE_DRIVER, OVERSTABLE_DRIVER
    );

    public static final List<DiscCategory> CHECK_ORDER_FOR_BOTH_EDGE_CASES = List.of(
            UNDERSTABLE_FAIRWAY_7, OVERSTABLE_MIDRANGE, STABLE_FAIRWAY_7, OVERSTABLE_APPROACH,
            STABLE_MIDRANGE, UNDERSTABLE_FAIRWAY_9, STABLE_FAIRWAY_9, UNDERSTABLE_DRIVER,
            OVERSTABLE_FAIRWAY_7, OVERSTABLE_FAIRWAY_9, STABLE_DRIVER, OVERSTABLE_DRIVER
    );

}
