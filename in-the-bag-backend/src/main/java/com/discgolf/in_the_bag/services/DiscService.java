package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.records.DiscAutoFillBaseRecord;
import com.discgolf.in_the_bag.records.DiscAutoFillRecord;
import com.discgolf.in_the_bag.records.PlasticRecord;
import com.discgolf.in_the_bag.repositories.DiscRepository;
import com.discgolf.in_the_bag.repositories.PlasticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class DiscService {
    private static final Logger logger = LoggerFactory.getLogger(BagService.class);

    private final DiscRepository discRepository;
    private final PlasticRepository plasticRepository;

    public Optional<DiscAutoFillRecord> getDiscDetailsForCreation(Long discId) {
        logger.info("Fetching base disc details for discId {}", discId);

        Optional<DiscAutoFillBaseRecord> baseDiscDetails = discRepository.findDiscDetailsForCreation(discId);

        if (baseDiscDetails.isEmpty()) {
            logger.warn("No base disc details found for discId {}", discId);
            return Optional.empty();
        }

        logger.info("Fetching plastics separately for discId {}", discId);
        List<PlasticRecord> plastics = plasticRepository.findPlasticsByManufacturer(baseDiscDetails.get().manufacturerId());

        // ✅ Combine into `DiscAutoFillRecord`
        return Optional.of(new DiscAutoFillRecord(
                baseDiscDetails.get().id(),
                baseDiscDetails.get().name(),
                baseDiscDetails.get().type(),
                baseDiscDetails.get().manufacturerName(),
                baseDiscDetails.get().speed(),
                baseDiscDetails.get().glide(),
                baseDiscDetails.get().turn(),
                baseDiscDetails.get().fade(),
                plastics  // ✅ Attach plastics list
        ));
    }
}

