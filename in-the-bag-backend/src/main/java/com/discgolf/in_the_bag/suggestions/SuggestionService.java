package com.discgolf.in_the_bag.suggestions;

import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.repositories.DiscInBagRepository;
import com.discgolf.in_the_bag.services.BagService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private static final Logger logger = LoggerFactory.getLogger(SuggestionService.class);

    private final DiscInBagRepository discInBagRepository;
    private final BagService bagService;
    private final SuggestionEngine suggestionEngine;

    public List<BagSuggestionDto> suggestForUser(Long userId, Long bagId) {
        logger.info("Fetching suggestions for user={} and bag={}", userId, bagId);

        // Validate ownership of the bag
        bagService.validateBagOwnership(userId, bagId);

        List<UserDisc> userDiscs = discInBagRepository.findUserDiscsByBagId(bagId);

        // Map them into BagSuggestionInputDtos
        List<BagSuggestionInputDto> suggestionInputDtos = userDiscs.stream()
                .map(BagSuggestionInputDto::from)
                .toList();

        List<BagSuggestionDto> suggestionDtos = suggestionEngine.suggestDiscs(suggestionInputDtos);
        logger.info("Found {} suggestions for user={}", suggestionDtos.size(), userId);

        return suggestionDtos;
    }

}

