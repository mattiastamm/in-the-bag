package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.models.Bag;
import com.discgolf.in_the_bag.models.DiscInBag;
import com.discgolf.in_the_bag.models.Plastic;
import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.BagWithDiscsDto;
import com.discgolf.in_the_bag.records.UserDiscDto;
import com.discgolf.in_the_bag.repositories.BagRepository;
import com.discgolf.in_the_bag.repositories.DiscInBagRepository;
import com.discgolf.in_the_bag.repositories.UserDiscRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BagService {
    private static final Logger logger = LoggerFactory.getLogger(BagService.class);

    private final BagRepository bagRepository;
    private final DiscInBagRepository discInBagRepository;
    private final UserDiscRepository userDiscRepository;

    private final UserDiscService userDiscService;

    public List<BagWithDiscsDto> getBagsWithDiscsForUser(Long userId) {
        logger.info("Fetching all bags with discs for userId {}", userId);

        List<Bag> bags = bagRepository.findByUserId(userId);
        logger.info("Found {} bags for userId {}", bags.size(), userId);

        return bags.stream().map(bag -> {
            List<UserDiscDto> discDtos = bag.getUserDiscs().stream().map(userDisc -> {
                Plastic plastic = userDisc.getPlastic();

                return new UserDiscDto(
                        userDisc.getUserDiscId(),
                        userDisc.getDisc().getName(),
                        userDisc.getDisc().getType(),
                        userDisc.getCustomSpeed(),
                        userDisc.getCustomGlide(),
                        userDisc.getCustomTurn(),
                        userDisc.getCustomFade(),
                        userDisc.getColor(),
                        plastic != null ? plastic.getId() : null,
                        plastic != null ? plastic.getName() : null,
                        userDisc.getCustomPlastic(),
                        userDisc.getDisc().getManufacturer().getName(),
                        userDisc.getDisc().getSpeed(),
                        userDisc.getDisc().getGlide(),
                        userDisc.getDisc().getTurn(),
                        userDisc.getDisc().getFade(),
                        userDisc.getWeight(),
                        userDisc.getInUse(),
                        userDisc.getComment()
                );
            }).toList();

            return new BagWithDiscsDto(
                    bag.getId(),
                    bag.getTitle(),
                    bag.getComment(),
                    bag.getCreatedAt(),
                    discDtos
            );
        }).toList();
    }

    public Bag createBag(Long userId, String title, String comment) {
        logger.info("Creating new bag for user={} with title={} and comment={}", userId, title, comment);

        // Fetch how many bags the user already has
        int bagCount = bagRepository.findByUserId(userId).size();

        // Business rule: Max 5 bags
        if (bagCount >= 5) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Maximum of 5 bags allowed per user"
            );
        }

        Bag bag = new Bag();
        bag.setUserId(userId);
        bag.setTitle(title);
        bag.setComment(comment);
        bag.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return bagRepository.save(bag);
    }


    @Transactional
    public void removeDiscFromBag(Long userId, Long userDiscId, Long bagId) {
        logger.info("Attempting to remove userDisc={} from bag={} for user={}", userDiscId, bagId, userId);

        userDiscService.validateUserDiscOwnership(userId, userDiscId);
        validateBagOwnership(userId, bagId);

        boolean exists = discInBagRepository.existsByUserDisc_UserDiscIdAndBag_Id(userDiscId, bagId);

        if (!exists) {
            logger.warn("No entry found in disc_in_bag for user_disc_id={} and bag_id={}", userDiscId, bagId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disc not found in the specified bag");
        }

        discInBagRepository.deleteByUserDisc_UserDiscIdAndBag_Id(userDiscId, bagId);
        logger.info("Removed user_disc_id={} from bag_id={}", userDiscId, bagId);

        // Check if the disc is still in any bag
        boolean stillInBags = discInBagRepository.existsByUserDisc_UserDiscId(userDiscId);
        if (!stillInBags) {
            UserDisc userDisc = userDiscRepository.findById(userDiscId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserDisc not found: " + userDiscId));
            userDisc.setInUse(false);
            userDiscRepository.save(userDisc);
            logger.info("Set in_use=false for user_disc_id={}", userDiscId);
        }
    }

    @Transactional
    public void updateBagDiscs(Long userId, Long bagId, List<Long> updatedUserDiscIds) {
        logger.info("Updating discs in bag with id={} to match user selection: {}", bagId, updatedUserDiscIds);

        for (Long userDiscId : updatedUserDiscIds) {
            userDiscService.validateUserDiscOwnership(userId, userDiscId);
        }

        // validate the request
        Bag bag = validateBagOwnershipAndReturn(userId, bagId);

        // Get current disc IDs in bag
        List<Long> currentDiscIds = discInBagRepository.findUserDiscIdsByBagId(bagId);

        Set<Long> currentSet = new HashSet<>(currentDiscIds);
        Set<Long> updatedSet = new HashSet<>(updatedUserDiscIds);
        logger.debug("Current discs in bag {}: {}", bagId, currentDiscIds);

        // Find discs to add and remove
        Set<Long> toAdd = new HashSet<>(updatedSet);
        toAdd.removeAll(currentSet);

        Set<Long> toRemove = new HashSet<>(currentSet);
        toRemove.removeAll(updatedSet);

        logger.info("Discs to add: {}", toAdd);
        logger.info("Discs to remove: {}", toRemove);

        // Deal with added discs
        for (Long userDiscId : toAdd) {
            logger.debug("Adding userDiscId={} to bagId={}", userDiscId, bagId);

            UserDisc userDisc = userDiscRepository.findById(userDiscId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserDisc not found: " + userDiscId));

            DiscInBag entry = new DiscInBag();
            entry.setUserDisc(userDisc);
            entry.setBag(bag);
            discInBagRepository.save(entry);
        }

        // Deal with removed discs
        for (Long userDiscId : toRemove) {
            logger.debug("Removing userDiscId={} from bagId={}", userDiscId, bagId);

            UserDisc userDisc = userDiscRepository.findById(userDiscId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserDisc not found: " + userDiscId));

            discInBagRepository.deleteByUserDiscAndBag(userDisc, bag);
        }

        // Update in_use for all affected discs
        Set<Long> affected = new HashSet<>();
        affected.addAll(toAdd);
        affected.addAll(toRemove);

        for (Long userDiscId : affected) {
            boolean stillInUse = discInBagRepository.existsByUserDisc_UserDiscId(userDiscId);
            userDiscService.setInUseStatus(userDiscId, stillInUse);
        }

        logger.info("Finished updating bag with id={}", bagId);
    }

    @Transactional
    public boolean deleteBag(Long userId, Long bagId) {
        logger.info("Attempting to delete bag with id={} and all related disc links.", bagId);
        validateBagOwnership(userId, bagId);

        // 1. Remove all disc links
        discInBagRepository.deleteByBag_Id(bagId);

        // 2. Delete the bag
        bagRepository.deleteById(bagId);

        logger.info("Bag with id={} successfully deleted.", bagId);
        return true;
    }



    // VALIDATION METHODS
    public void validateBagOwnership(Long userId, Long bagId) {
        Bag bag = bagRepository.findById(bagId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bag with id=" + bagId + " not found"));

        if (!bag.getUserId().equals(userId)) {
            logger.warn("Bag with id={} does not belong to user with id={}", bagId, userId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this bag");
        }
    }

    public Bag validateBagOwnershipAndReturn(Long userId, Long bagId) {
        Bag bag = bagRepository.findById(bagId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bag with id=" + bagId + " not found"));

        if (!bag.getUserId().equals(userId)) {
            logger.warn("Bag with id={} does not belong to user with id={}", bagId, userId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this bag");
        }

        return bag;
    }

}
