package com.discgolf.in_the_bag.suggestions;

import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.repositories.DiscInBagRepository;
import com.discgolf.in_the_bag.repositories.SuggestionRepository;
import com.discgolf.in_the_bag.services.BagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final DiscInBagRepository discInBagRepository;
    private final BagService bagService;
    private final SuggestionEngine suggestionEngine;
    private final SuggestionRepository suggestionRepository;

    public List<BagSuggestionDto> suggestForUser(Long userId, Long bagId) {
        // Validate ownership of the bag
        bagService.validateBagOwnership(userId, bagId);

        List<UserDisc> userDiscs = discInBagRepository.findUserDiscsByBagId(bagId);

        // Map them into BagSuggestionInputDtos
        List<BagSuggestionInputDto> suggestionInputDtos = userDiscs.stream()
                .map(BagSuggestionInputDto::from)
                .toList();

        return suggestionEngine.suggestDiscs(suggestionInputDtos);
    }

}

