package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.models.Bag;
import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.BagRecord;
import com.discgolf.in_the_bag.records.BagWithDiscsDto;
import com.discgolf.in_the_bag.records.UserDiscDto;
import com.discgolf.in_the_bag.repositories.BagRepository;
import com.discgolf.in_the_bag.repositories.DiscInBagRepository;
import com.discgolf.in_the_bag.repositories.UserDiscRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@RequiredArgsConstructor
public class BagService {
    private static final Logger logger = LoggerFactory.getLogger(BagService.class);

    private final BagRepository bagRepository;
    private final DiscInBagRepository discInBagRepository;
    private final UserDiscRepository userDiscRepository;

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
            List<UserDiscDto> discDtos = bag.getUserDiscs().stream().map(userDisc ->
                    new UserDiscDto(
                            userDisc.getUserDiscId(),
                            userDisc.getDisc().getName(),
                            userDisc.getDisc().getType(),
                            userDisc.getCustomSpeed(),
                            userDisc.getCustomGlide(),
                            userDisc.getCustomTurn(),
                            userDisc.getCustomFade(),
                            userDisc.getColor(),
                            userDisc.getPlastic().getName(),
                            userDisc.getDisc().getManufacturer().getName(),
                            userDisc.getDisc().getSpeed(),
                            userDisc.getDisc().getGlide(),
                            userDisc.getDisc().getTurn(),
                            userDisc.getDisc().getFade(),
                            userDisc.getInUse()
                    )
            ).toList();

            return new BagWithDiscsDto(
                    bag.getId(),
                    bag.getTitle(),
                    bag.getComment(),
                    bag.getCreatedAt(),
                    discDtos
            );
        }).toList();
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

}
