package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.models.Suggestion;
import com.discgolf.in_the_bag.models.Wishlist;
import com.discgolf.in_the_bag.models.User;
import com.discgolf.in_the_bag.records.WishlistAddRequest;
import com.discgolf.in_the_bag.records.WishlistDiscDto;
import com.discgolf.in_the_bag.repositories.SuggestionRepository;
import com.discgolf.in_the_bag.repositories.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private static final Logger logger = LoggerFactory.getLogger(WishlistService.class);

    private final WishlistRepository wishlistRepository;
    private final SuggestionRepository suggestionRepository;

    public List<WishlistDiscDto> getWishlistDiscs(Long userId) {
        logger.info("Fetching wishlist discs for userId={}", userId);
        return wishlistRepository.findWishlistDiscsByUserId(userId);
    }

    public void addToWishlist(Long userId, WishlistAddRequest addRequest) {
        List<Long> incomingSuggestionIds = addRequest.suggestionIds();
        logger.info("Attempting to add {} suggestions to wishlist for userId={}", incomingSuggestionIds.size(), userId);

        // Optional: validate that all suggestionIds exist
        validateDiscInSuggestionList(incomingSuggestionIds);

        // Fetch already existing suggestion IDs for this user
        Set<Long> existingSuggestionIds = wishlistRepository.findAllSuggestionIdsByUserId(userId);

        // Filter out duplicates
        List<Long> newSuggestionIds = incomingSuggestionIds.stream()
                .filter(id -> !existingSuggestionIds.contains(id))
                .toList();

        if (newSuggestionIds.isEmpty()) {
            logger.info("No new suggestions to add for userId={}", userId);
            return;
        }

        // Fetch only the Suggestion entities that are new
        List<Suggestion> newSuggestions = suggestionRepository.findAllById(newSuggestionIds);

        // Create a User object with only the ID
        User user = new User();
        user.setId(userId);

        // Map to Wishlist entries
        List<Wishlist> wishlistEntries = newSuggestions.stream()
                .map(suggestion -> Wishlist.builder()
                        .user(user)
                        .suggestion(suggestion)
                        .addedAt(LocalDateTime.now().toString()) // or your date format
                        .build())
                .toList();

        // Save new entries
        wishlistRepository.saveAll(wishlistEntries);

        logger.info("Added {} new suggestions to wishlist for userId={}", wishlistEntries.size(), userId);
    }


    @Transactional
    public void removeFromWishlist(Long userId, Long suggestionId) {
        logger.info("Attempting to delete suggestion={} from wishlist for userId={}", suggestionId, userId);

        if (!wishlistRepository.existsByUserIdAndSuggestionId(userId, suggestionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Wishlist entry for suggestionId=" + suggestionId + " and userId=" + userId + " does not exist."
            );
        }
        wishlistRepository.deleteByUserIdAndSuggestionId(userId, suggestionId);
    }


    // Checks if all suggestion_ids are valid -> are in the suggestions table
    private void validateDiscInSuggestionList(List<Long> suggestionIds) {
        Set<Long> validSuggestionIds = suggestionRepository.findAllIds();

        if (!validSuggestionIds.containsAll(suggestionIds)) {
            logger.warn("Some suggestionIds are not in the official suggestion set");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One or more discs are not from the official suggestion list."
            );
        }
    }

}

