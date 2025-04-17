package com.discgolf.in_the_bag.suggestions;

import com.discgolf.in_the_bag.models.Disc;
import com.discgolf.in_the_bag.services.DiscService;
import com.discgolf.in_the_bag.services.UserDiscService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SuggestionEngine {
    private static final Logger logger = LoggerFactory.getLogger(UserDiscService.class);
    private final DiscSuggestionLoader suggestionLoader;
    private final DiscService discService;

    public List<BagSuggestionDto> suggestDiscs(List<BagSuggestionInputDto> suggestionInputDtos) {
        Map<DiscCategory, List<BagSuggestionInputDto>> discsByCategory = generateMap();
        categorizeDiscs(suggestionInputDtos, discsByCategory);

        boolean understable_putt_approach_in_bag = (!discsByCategory.get(DiscCategory.UNDERSTABLE_PUTT_APPROACH).isEmpty());
        boolean understable_midrange_in_bag = (!discsByCategory.get(DiscCategory.UNDERSTABLE_MIDRANGE).isEmpty());
        int state = (understable_putt_approach_in_bag ? 1 : 0) + (understable_midrange_in_bag ? 2 : 0);

        // determine the order of checking
        List<DiscCategory> checkOrder = resolveCheckOrder(state);
        resolveFairwaySpecialCases(discsByCategory, checkOrder);

        List<DiscCategory> suggestion_categories = new ArrayList<>();

        // Add 2 missing categories to suggestions based on the correct order
        for (DiscCategory category : checkOrder) {
            if (suggestion_categories.size() >= 2) break;
            if (discsByCategory.get(category).isEmpty()) suggestion_categories.add(category);
        }

        List<BagSuggestionDto> suggestions = new ArrayList<>();

        // find actual discs for suggestions from pre-processed datasets
        for (DiscCategory category : suggestion_categories) {
            List<Long> discIds = suggestionLoader.getSuggestionsForCategory(category);
            String categoryLabel = suggestionLoader.getLabelForCategory(category);
            List<DiscSuggestionDto> discSuggestionDtos = discService.getDiscSuggestionDtosByIds(discIds);

            BagSuggestionDto bagSuggestionDto = new BagSuggestionDto(categoryLabel, discSuggestionDtos);
            suggestions.add(bagSuggestionDto);
        }

        return suggestions;
    }

    private static Map<DiscCategory, List<BagSuggestionInputDto>> generateMap() {
        Map<DiscCategory, List<BagSuggestionInputDto>> filterMap = new HashMap<>();

        for (DiscCategory category : DiscCategory.values()) {
            filterMap.put(category, new ArrayList<>());
        }

        return filterMap;
    }

    private static void categorizeDiscs(List<BagSuggestionInputDto> discDtos, Map<DiscCategory, List<BagSuggestionInputDto>> filterMap) {
        for (BagSuggestionInputDto discDto : discDtos) {
            String category = discDto.type().toLowerCase();
            float speed = discDto.speed();
            float turn = discDto.turn();
            float fade = discDto.fade();
            float stability = turn + fade;

            DiscCategory discCategory = resolveDiscCategory(discDto.userDiscId(), category, speed, turn, stability);
            filterMap.get(discCategory).add(discDto);
        }
    }

    private static DiscCategory resolveDiscCategory(Long discId, String category, float speed, float turn, float stability) {
        boolean isUnderstable = (turn <= -2) || (turn > -2 && turn < 0 && stability <= -1);
        boolean isOverstable = (turn >= 0 && stability > 2) || (turn > -2 && turn < 0 && stability >= 3);

        switch (category) {
            case "putt & approach":
                if (isUnderstable) return DiscCategory.UNDERSTABLE_PUTT_APPROACH;
                if (isOverstable) return DiscCategory.OVERSTABLE_APPROACH;
                return DiscCategory.STABLE_PUTT_APPROACH;

            case "midrange":
                if (isUnderstable) return DiscCategory.UNDERSTABLE_MIDRANGE;
                if (isOverstable) return speed == 4 ? DiscCategory.OVERSTABLE_APPROACH : DiscCategory.OVERSTABLE_MIDRANGE;
                return DiscCategory.STABLE_MIDRANGE;

            case "fairway driver":
                return resolveFairwayCategory(discId, speed, isUnderstable, isOverstable);

            case "distance driver":
                if (isUnderstable) return DiscCategory.UNDERSTABLE_DRIVER;
                if (isOverstable) return DiscCategory.OVERSTABLE_DRIVER;
                return DiscCategory.STABLE_DRIVER;

            default:
                logger.warn("disc with id={} has invalid type={}", discId, category);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "disc with id={" + discId + "} has invalid type={" + category + "}"
                );
        }
    }

    private static DiscCategory resolveFairwayCategory(Long discId, float speed, boolean isUnderstable, boolean isOverstable) {
        int speedInt = (int) speed;
        if (speedInt < 6 || speedInt > 9) {
            logger.warn("disc with id={} has invalid speed={}", discId, speed);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "disc with id={" + discId + "} has invalid speed={" + speed + "}"
            );
        }

        if (isUnderstable) {
            return switch (speedInt) {
                case 6 -> DiscCategory.UNDERSTABLE_FAIRWAY_6;
                case 7 -> DiscCategory.UNDERSTABLE_FAIRWAY_7;
                case 8 -> DiscCategory.UNDERSTABLE_FAIRWAY_8;
                default -> DiscCategory.UNDERSTABLE_FAIRWAY_9;
            };
        }

        if (isOverstable) {
            return switch (speedInt) {
                case 6 -> DiscCategory.OVERSTABLE_FAIRWAY_6;
                case 7 -> DiscCategory.OVERSTABLE_FAIRWAY_7;
                case 8 -> DiscCategory.OVERSTABLE_FAIRWAY_8;
                default -> DiscCategory.OVERSTABLE_FAIRWAY_9;
            };
        }

        return switch (speedInt) {
            case 6 -> DiscCategory.STABLE_FAIRWAY_6;
            case 7 -> DiscCategory.STABLE_FAIRWAY_7;
            case 8 -> DiscCategory.STABLE_FAIRWAY_8;
            default -> DiscCategory.STABLE_FAIRWAY_9;
        };
    }

    private static List<DiscCategory> resolveCheckOrder(int state) {
        switch (state) {
            case 0:  return DiscCategory.CHECK_ORDER_SIMPLE;
            case 1:  return DiscCategory.CHECK_ORDER_FOR_UNDERSTABLE_PUTT_APPROACH;
            case 2:  return DiscCategory.CHECK_ORDER_FOR_UNDERSTABLE_MIDRANGE;
            default: return DiscCategory.CHECK_ORDER_FOR_BOTH_EDGE_CASES;
        }
    }

    // if a user has a speed 6 fairway, we don't recommend speed 7 of the same stability.
    //If a user has a speed 8 fairway, we don't recommend speed 7 or speed 9 of the same stability.
    private static void resolveFairwaySpecialCases(Map<DiscCategory, List<BagSuggestionInputDto>> discsByCategory, List<DiscCategory> order) {
        boolean understable_8 = !discsByCategory.get(DiscCategory.UNDERSTABLE_FAIRWAY_8).isEmpty();
        boolean stable_8 = !discsByCategory.get(DiscCategory.STABLE_FAIRWAY_8).isEmpty();
        boolean overstable_8 = !discsByCategory.get(DiscCategory.OVERSTABLE_FAIRWAY_8).isEmpty();
        boolean understable_6 = !discsByCategory.get(DiscCategory.UNDERSTABLE_FAIRWAY_6).isEmpty();
        boolean stable_6 = !discsByCategory.get(DiscCategory.STABLE_FAIRWAY_6).isEmpty();
        boolean overstable_6 = !discsByCategory.get(DiscCategory.OVERSTABLE_FAIRWAY_6).isEmpty();

        int understable = (understable_6 ? 1 : 0) + (understable_8 ? 2 : 0);
        int stable = (stable_6 ? 1 : 0) + (stable_8 ? 2 : 0);
        int overstable = (overstable_6 ? 1 : 0) + (overstable_8 ? 2 : 0);

        if (understable > 1) {
            order.remove(DiscCategory.UNDERSTABLE_FAIRWAY_7);
            order.remove(DiscCategory.UNDERSTABLE_FAIRWAY_9);
        } else if (understable == 1) {
            order.remove(DiscCategory.UNDERSTABLE_FAIRWAY_7);
        }

        if (stable > 1) {
            order.remove(DiscCategory.STABLE_FAIRWAY_7);
            order.remove(DiscCategory.STABLE_FAIRWAY_9);
        } else if (stable == 1) {
            order.remove(DiscCategory.STABLE_FAIRWAY_7);
        }

        if (overstable > 1) {
            order.remove(DiscCategory.OVERSTABLE_FAIRWAY_7);
            order.remove(DiscCategory.OVERSTABLE_FAIRWAY_9);
        } else if (overstable == 1) {
            order.remove(DiscCategory.OVERSTABLE_FAIRWAY_7);
        }
    }

}
