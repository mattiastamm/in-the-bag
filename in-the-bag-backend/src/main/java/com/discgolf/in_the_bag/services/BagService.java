package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.models.Bag;
import com.discgolf.in_the_bag.models.DiscInBag;
import com.discgolf.in_the_bag.models.Plastic;
import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.BagRecord;
import com.discgolf.in_the_bag.records.BagWithDiscsDto;
import com.discgolf.in_the_bag.records.CreateBagRequest;
import com.discgolf.in_the_bag.records.UserDiscDto;
import com.discgolf.in_the_bag.repositories.BagRepository;
import com.discgolf.in_the_bag.repositories.DiscInBagRepository;
import com.discgolf.in_the_bag.repositories.UserDiscRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@RequiredArgsConstructor
public class BagService {
    private static final Logger logger = LoggerFactory.getLogger(BagService.class);

    private final BagRepository bagRepository;
    private final DiscInBagRepository discInBagRepository;
    private final UserDiscRepository userDiscRepository;

    private final UserDiscService userDiscService;

    public List<BagRecord> getBagsByUserDiscId(Long userDiscId) {
        logger.info("Fetching bags containing userDiscId {}", userDiscId);

        List<BagRecord> bags = bagRepository.findBagsByUserDiscId(userDiscId);
        logger.info("Found {} bags for userDiscId {}", bags.size(), userDiscId);
        return bags;
    }

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

    public Bag createBag(CreateBagRequest request) {
        Bag bag = new Bag();
        bag.setUserId(request.userId());
        bag.setTitle(request.title());
        bag.setComment(request.comment());
        bag.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return bagRepository.save(bag);
    }


    @Transactional
    public void removeDiscFromBag(Long userDiscId, Long bagId) {
        boolean exists = discInBagRepository.existsByUserDisc_UserDiscIdAndBag_Id(userDiscId, bagId);

        if (!exists) {
            logger.warn("No entry found in disc_in_bag for user_disc_id={} and bag_id={}", userDiscId, bagId);
            return; // could also throw a custom exception here
        }

        discInBagRepository.deleteByUserDisc_UserDiscIdAndBag_Id(userDiscId, bagId);
        logger.info("Removed user_disc_id={} from bag_id={}", userDiscId, bagId);

        // Check if the disc is still in any bag
        boolean stillInBags = discInBagRepository.existsByUserDisc_UserDiscId(userDiscId);
        if (!stillInBags) {
            UserDisc userDisc = userDiscRepository.findById(userDiscId)
                    .orElseThrow(() -> new EntityNotFoundException("UserDisc not found: " + userDiscId));
            userDisc.setInUse(false);
            userDiscRepository.save(userDisc);
            logger.info("Set in_use=false for user_disc_id={}", userDiscId);
        }
    }

    @Transactional
    public void updateBagDiscs(Long bagId, List<Long> updatedUserDiscIds) {
        logger.info("Updating discs in bag with id={} to match user selection: {}", bagId, updatedUserDiscIds);

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

        Bag bag = bagRepository.findById(bagId)
                .orElseThrow(() -> new RuntimeException("Bag not found: " + bagId));

        // Deal with added discs
        for (Long userDiscId : toAdd) {
            logger.debug("Adding userDiscId={} to bagId={}", userDiscId, bagId);

            UserDisc userDisc = userDiscRepository.findById(userDiscId)
                    .orElseThrow(() -> new RuntimeException("UserDisc not found: " + userDiscId));

            DiscInBag entry = new DiscInBag();
            entry.setUserDisc(userDisc);
            entry.setBag(bag);
            discInBagRepository.save(entry);
        }

        // Deal with removed discs
        for (Long userDiscId : toRemove) {
            logger.debug("Removing userDiscId={} from bagId={}", userDiscId, bagId);

            UserDisc userDisc = userDiscRepository.findById(userDiscId)
                    .orElseThrow(() -> new RuntimeException("UserDisc not found: " + userDiscId));

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
    public boolean deleteBag(Long bagId) {
        logger.info("Attempting to delete bag with id={} and all related disc links.", bagId);
        if (!bagRepository.existsById(bagId)) {
            logger.warn("Bag not found with id={}.", bagId);
            return false;
        }

        // 1. Remove all disc links
        discInBagRepository.deleteByBag_Id(bagId);

        // 2. Delete the bag
        bagRepository.deleteById(bagId);

        logger.info("Bag with id={} successfully deleted.", bagId);
        return true;
    }

}
