package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.models.Disc;
import com.discgolf.in_the_bag.models.Plastic;
import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.*;
import com.discgolf.in_the_bag.repositories.BagRepository;
import com.discgolf.in_the_bag.repositories.DiscRepository;
import com.discgolf.in_the_bag.repositories.UserDiscRepository;
import com.discgolf.in_the_bag.repositories.PlasticRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserDiscService {
    private static final Logger logger = LoggerFactory.getLogger(BagService.class);

    private final UserDiscRepository userDiscRepository;
    private final BagRepository bagRepository;
    private final PlasticRepository plasticRepository;
    private final DiscRepository discRepository;

    public UserDiscService(UserDiscRepository userDiscRepository, BagRepository bagRepository, PlasticRepository plasticRepository, DiscRepository discRepository) {
        this.userDiscRepository = userDiscRepository;
        this.bagRepository = bagRepository;
        this.plasticRepository = plasticRepository;
        this.discRepository = discRepository;
    }

    public List<UserDiscDto> getUserDiscs(Long userId) {
        logger.info("Fetching base view DTO for every disc for userId={}", userId);
        return userDiscRepository.findUserDiscsByUserId(userId);
    }

    public DiscDetailsRecord getDiscDetails(Long userDiscId) {
        logger.info("Fetching detailed view DTO for userDiscId={}", userDiscId);

        // Fetch disc details (without bags)
        Optional<UserDiscDto> userDiscDtoOpt = userDiscRepository.findUserDiscsById(userDiscId);

        if (userDiscDtoOpt.isEmpty()) {
            logger.warn("Disc not found for userDiscId={}", userDiscId);
            throw new RuntimeException("Disc not found for user_discs.id: " + userDiscId);
        }

        UserDiscDto baseDiscDetails = userDiscDtoOpt.get();

        // Fetch associated bags
        List<BagRecord> bags = bagRepository.findBagsByUserDiscId(userDiscId);

        // ✅ Fetch the plastics for the disc’s manufacturer
        List<PlasticRecord> plastics = userDiscRepository.findPlasticsByUserDiscId(userDiscId);

        // Merge into `DiscDetailsRecord`
        return new DiscDetailsRecord(
                baseDiscDetails.userDiscId(),
                baseDiscDetails.name(),
                baseDiscDetails.type(),
                baseDiscDetails.customSpeed(),
                baseDiscDetails.customGlide(),
                baseDiscDetails.customTurn(),
                baseDiscDetails.customFade(),
                baseDiscDetails.color(),
                baseDiscDetails.plasticId(),
                baseDiscDetails.plasticName(),
                baseDiscDetails.manufacturerName(),
                baseDiscDetails.speed(),
                baseDiscDetails.glide(),
                baseDiscDetails.turn(),
                baseDiscDetails.fade(),
                baseDiscDetails.weight(),
                baseDiscDetails.inUse(),
                baseDiscDetails.comment(),
                bags,
                plastics
        );
    }

    public UserDisc addDiscToUser(CreateUserDiscRequest request) {
        logger.info("Adding new disc for userId={} with discId={} and plasticId={}",
                request.userId(), request.discId(), request.plasticId());

        // Validate referenced entities (disc & plastic exist)
        Disc disc = discRepository.findById(request.discId())
                .orElseThrow(() -> new RuntimeException("Disc not found"));
        Plastic plastic = plasticRepository.findById(request.plasticId())
                .orElseThrow(() -> new RuntimeException("Plastic not found"));

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Create new UserDisc entity
        UserDisc newUserDisc = new UserDisc();
        newUserDisc.setUserId(request.userId());
        newUserDisc.setDisc(disc);
        newUserDisc.setPlastic(plastic);
        newUserDisc.setColor(request.color());
        newUserDisc.setWeight(request.weight());
        newUserDisc.setCustomSpeed(request.customSpeed());
        newUserDisc.setCustomGlide(request.customGlide());
        newUserDisc.setCustomTurn(request.customTurn());
        newUserDisc.setCustomFade(request.customFade());
        newUserDisc.setComment(request.comment());
        newUserDisc.setInUse(false); // Default: not in use
        newUserDisc.setUpdatedAt(currentTime); // Default: current time

        // Save & return new disc
        return userDiscRepository.save(newUserDisc);
    }

    public boolean updateDisc(Long userDiscId, UpdateDiscRequest request) {
        logger.info("Updating userDiscId={}", userDiscId);

        UserDisc userDisc = userDiscRepository.findDiscEntityByUserDiscId(userDiscId)
                .orElseThrow(() -> new RuntimeException("Disc not found or does not belong to user"));

        // ✅ Update only provided fields
        if (request.customSpeed() != null) userDisc.setCustomSpeed(request.customSpeed());
        if (request.customGlide() != null) userDisc.setCustomGlide(request.customGlide());
        if (request.customTurn() != null) userDisc.setCustomTurn(request.customTurn());
        if (request.customFade() != null) userDisc.setCustomFade(request.customFade());
        if (request.color() != null) userDisc.setColor(request.color());
        if (request.weight() != null && request.weight() >= 1) userDisc.setWeight(request.weight());
        userDisc.setComment(request.comment());

        // ✅ Update plastic if changed
        if (request.plasticId() != null) {
            Plastic newPlastic = plasticRepository.findPlasticEntityById(request.plasticId())
                    .orElseThrow(() -> new RuntimeException("Plastic not found"));
            userDisc.setPlastic(newPlastic);
        }

        // ✅ Save updated disc
        userDiscRepository.save(userDisc);
        return true;
    }

    public boolean deleteDisc(Long userDiscId) {
        logger.info("Attempting to delete userDiscId={}", userDiscId);

        if (!userDiscRepository.existsById(userDiscId)) {
            logger.warn("UserDisc with id={} not found", userDiscId);
            return false; // ❌ Disc does not exist
        }

        userDiscRepository.deleteById(userDiscId); // ✅ Delete disc
        return true; // ✅ Successful deletion
    }

    public void setInUseStatus(Long userDiscId, boolean inUse) {
        logger.info("Setting in_use value for user userDiscId={} to inUse={}", userDiscId, inUse);
        userDiscRepository.updateInUseStatus(userDiscId, inUse);
    }

}
