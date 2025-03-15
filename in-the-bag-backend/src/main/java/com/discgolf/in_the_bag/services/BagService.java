package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.records.BagRecord;
import com.discgolf.in_the_bag.repositories.BagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BagService {
    private final BagRepository bagRepository;

    public List<BagRecord> getBagsByUserDiscId(Long userDiscId) {
        return bagRepository.findBagsByUserDiscId(userDiscId);
    }
}
