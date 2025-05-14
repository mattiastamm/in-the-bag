package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.models.Disc;
import com.discgolf.in_the_bag.models.Plastic;
import com.discgolf.in_the_bag.models.User;
import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.*;
import com.discgolf.in_the_bag.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserDiscService {
    private static final Logger logger = LoggerFactory.getLogger(UserDiscService.class);

    private final UserDiscRepository userDiscRepository;
    private final DiscInBagRepository discInBagRepository;
    private final PlasticRepository plasticRepository;
    private final DiscRepository discRepository;
    private final UserRepository userRepository;

    public List<UserDiscDto> getUserDiscs(Long userId) {
        logger.info("Fetching base view DTO for every disc for userId={}", userId);
        return userDiscRepository.findUserDiscsByUserId(userId);
    }

    public DiscDetailsRecord getDiscDetails(Long userId, Long userDiscId) {
        logger.info("Fetching detailed view DTO for userDiscId={}", userDiscId);
        validateUserDiscOwnership(userId, userDiscId);

        // Fetch disc details (without bags)
        Optional<UserDiscDto> userDiscDtoOpt = userDiscRepository.findUserDiscById(userDiscId);

        if (userDiscDtoOpt.isEmpty()) {
            logger.warn("Disc not found for userDiscId={}", userDiscId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserDisc not found: " + userDiscId);
        }

        UserDiscDto baseDiscDetails = userDiscDtoOpt.get();

        // Fetch associated bags
        List<BagRecord> bags = discInBagRepository.findBagsByUserDiscId(userDiscId);

        // Fetch the plastics for the disc’s manufacturer
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
                baseDiscDetails.customPlastic(),
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

    public UserDisc addDiscToUser(Long userId, CreateUserDiscRequest request) {
        logger.info("Adding new disc for userId={} with discId={} and plasticId={}/customPlastic={}",
                userId, request.discId(), request.plasticId(), request.customPlastic());

        // Validate referenced entities
        Disc disc = discRepository.findById(request.discId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disc not found: " + request.discId()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));

        // validate plastics
        validatePlasticChoice(request.plasticId(), request.customPlastic());

        // if plasticId exists, we find the corresponding plastic; else we use the customPlastic string
        Plastic plastic = null;
        if (request.plasticId() != null){
            plastic = plasticRepository.findById(request.plasticId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plastic not found: " + request.plasticId()));
        }

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Create new UserDisc entity
        UserDisc newUserDisc = new UserDisc();
        newUserDisc.setUser(user);
        newUserDisc.setDisc(disc);
        newUserDisc.setPlastic(plastic);
        newUserDisc.setCustomPlastic(request.customPlastic());
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

    @Transactional
    public boolean updateUserDisc(Long userId, Long userDiscId, UpdateDiscRequest request) {
        logger.info("Updating userDiscId={}", userDiscId);

        // validate the request
        UserDisc userDisc = validateUserDiscOwnershipAndReturn(userId, userDiscId);

        // Update fields
        userDisc.setCustomSpeed(request.customSpeed());
        userDisc.setCustomGlide(request.customGlide());
        userDisc.setCustomTurn(request.customTurn());
        userDisc.setCustomFade(request.customFade());
        userDisc.setColor(request.color());
        userDisc.setWeight(request.weight());
        userDisc.setComment(request.comment());

        // Validate & update plastic if changed
        validatePlasticChoice(request.plasticId(), request.customPlastic());

        if (request.plasticId() != null) {
            Plastic newPlastic = plasticRepository.findPlasticEntityById(request.plasticId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plastic not found: " + request.plasticId()));
            userDisc.setPlastic(newPlastic);
            userDisc.setCustomPlastic(null); // Clear custom plastic
        }
        if (request.customPlastic() != null) {
            userDisc.setPlastic(null); // Clear predefined plastic
            userDisc.setCustomPlastic(request.customPlastic());
        }

        // Save updated disc
        userDiscRepository.save(userDisc);
        return true;
    }

    public void deleteUserDisc(Long userId, Long userDiscId) {
        logger.info("Attempting to delete userDiscId={}", userDiscId);
        validateUserDiscOwnership(userId, userDiscId);
        userDiscRepository.deleteById(userDiscId); // ✅ Delete disc
    }

    // NB! No need to check if userDiscId is valid, done before
    public void setInUseStatus(Long userDiscId, boolean inUse) {
        logger.info("Setting in_use value for user userDiscId={} to inUse={}", userDiscId, inUse);
        userDiscRepository.updateInUseStatus(userDiscId, inUse);
    }



    // VALIDATION METHODS
    public void validateUserDiscOwnership(Long userId, Long userDiscId) {
        logger.info("Validating userDisc={} ownership", userDiscId);

        UserDisc userDisc = userDiscRepository.findById(userDiscId)
                .orElseThrow(() -> {
                    logger.warn("userDisc with id={} does not exist", userDiscId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "UserDisc with this id does not exist");
                });

        if (!userDisc.getUser().getId().equals(userId)) {
            logger.warn("userDisc with id={} does not belong to user with userId={}", userDiscId, userId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "UserDisc does not belong to this user");
        }
    }

    public UserDisc validateUserDiscOwnershipAndReturn(Long userId, Long userDiscId) {
        logger.info("Validating userDisc={} ownership", userDiscId);

        UserDisc userDisc = userDiscRepository.findById(userDiscId)
                .orElseThrow(() -> {
                    logger.warn("userDisc with id={} does not exist", userDiscId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "UserDisc with this id does not exist");
                });

        if (!userDisc.getUser().getId().equals(userId)) {
            logger.warn("userDisc with id={} does not belong to user with userId={}", userDiscId, userId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "UserDisc does not belong to this user");
        }

        return userDisc;
    }

    private void validatePlasticChoice(Long plasticId, String customPlastic) {
        if (plasticId != null && customPlastic != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only one of plasticId or customPlastic should be provided."
            );
        }

        if (plasticId == null && customPlastic == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You must provide either a plasticId or a customPlastic."
            );
        }
    }


}
