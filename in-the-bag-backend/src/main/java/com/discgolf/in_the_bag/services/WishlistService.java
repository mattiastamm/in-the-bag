package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.models.Disc;
import com.discgolf.in_the_bag.models.WishlistEntry;
import com.discgolf.in_the_bag.records.WishlistDiscDto;
import com.discgolf.in_the_bag.repositories.DiscRepository;
import com.discgolf.in_the_bag.repositories.WishlistRepository;
import com.discgolf.in_the_bag.suggestions.DiscSuggestionLoader;
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
    private final DiscSuggestionLoader discSuggestionLoader;
    private final DiscRepository discRepository;

    public List<WishlistDiscDto> getWishlistDiscs(Long userId) {
        logger.info("Attempting to fetch wishlist discs for userId={}", userId);

        List<WishlistEntry> wishlist = wishlistRepository.findByUserId(userId);
        return wishlist.stream()
                .map(wishlistEntry -> new WishlistDiscDto(
                        wishlistEntry.getDisc().getId(),
                        wishlistEntry.getDisc().getName(),
                        wishlistEntry.getDisc().getManufacturer().getName(),
                        wishlistEntry.getDisc().getSpeed(),
                        wishlistEntry.getDisc().getGlide(),
                        wishlistEntry.getDisc().getTurn(),
                        wishlistEntry.getDisc().getFade(),
                        wishlistEntry.getDisc().getType()
                ))
                .toList();
    }


    public void addDiscs(Long userId, List<Long> discIds) {
        logger.info("Attempting to add {} discs to wishlist for userId={}", discIds.size(), userId);

        // Validate all discIds first
        validateDiscInSuggestionList(discIds);

        // Remove duplicates (already in wishlist)
        List<Long> alreadyInWishlist = wishlistRepository.findDiscIdsByUserId(userId);

        List<WishlistEntry> newEntries = discIds.stream()
                .filter(id -> !alreadyInWishlist.contains(id))
                .map(id -> {
                    Disc disc = discRepository.findById(id)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disc not found: " + id));

                    WishlistEntry entry = new WishlistEntry();
                    entry.setUserId(userId);
                    entry.setDisc(disc);
                    entry.setAddedAt(LocalDateTime.now().toString());

                    return entry;
                })
                .toList();

        // Save new entries
        wishlistRepository.saveAll(newEntries);
        logger.info("Added {} new discs to wishlist for userId={}", newEntries.size(), userId);
    }

    public void removeFromWishlist(Long userId, Long discId) {
        logger.info("Attempting to delete disc={} from wishlist for userId={}", discId, userId);

        if (!wishlistRepository.existsByUserIdAndDisc_Id(userId, discId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Wishlist entry for discId=" + discId + " and userId=" + userId + " does not exist."
            );
        }
        wishlistRepository.deleteByUserIdAndDisc_Id(userId, discId);
    }


    // Checks if all disc_ids are valid -> are listed somewhere in the disc_suggestions.json
    private void validateDiscInSuggestionList(List<Long> discIds) {
        Set<Long> validSuggestionIds = discSuggestionLoader.getAllSuggestionDiscIds();

        if (!validSuggestionIds.containsAll(discIds)) {
            logger.warn("Some discIds are not in the official suggestion set: {}", discIds);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One or more discs are not from the official suggestion list."
            );
        }
    }

}

