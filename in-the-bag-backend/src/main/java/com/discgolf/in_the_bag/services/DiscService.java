package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.models.Plastic;
import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.*;
import com.discgolf.in_the_bag.repositories.BagRepository;
import com.discgolf.in_the_bag.repositories.DiscRepository;
import com.discgolf.in_the_bag.repositories.PlasticRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DiscService {
    private final DiscRepository discRepository;
    private final BagRepository bagRepository;
    private final PlasticRepository plasticRepository;

    public DiscService(DiscRepository discRepository, BagRepository bagRepository, PlasticRepository plasticRepository) {
        this.discRepository = discRepository;
        this.bagRepository = bagRepository;
        this.plasticRepository = plasticRepository;
    }

    // ✅ fetches base view DTO for every disc the user has
    public List<InventoryDiscRecord> getUserDiscs(Long userId) {
        return discRepository.findDiscsByUserId(userId);
    }

    // ✅ fetches detailed view DTO for disc based on ID
    public DiscDetailsRecord getDiscDetails(Long userDiscId) {
        // Fetch disc details (without bags)
        Optional<BaseDiscDetailsRecord> baseDiscDetailsOpt = discRepository.findBaseDiscDetailsById(userDiscId);

        if (baseDiscDetailsOpt.isEmpty()) {
            throw new RuntimeException("Disc not found for user_discs.id: " + userDiscId);
        }

        BaseDiscDetailsRecord baseDiscDetails = baseDiscDetailsOpt.get();

        // Fetch associated bags
        List<BagRecord> bags = bagRepository.findBagsByUserDiscId(userDiscId);

        // ✅ Fetch the plastics for the disc’s manufacturer
        List<PlasticRecord> plastics = discRepository.findPlasticsByUserDiscId(userDiscId);

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

    public boolean updateDisc(Long userDiscId, UpdateDiscRequest request) {
        UserDisc userDisc = discRepository.findDiscEntityByUserDiscId(userDiscId)
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
        discRepository.save(userDisc);
        return true;
    }

}
