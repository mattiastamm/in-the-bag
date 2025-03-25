package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.models.DiscInBag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscInBagRepository extends JpaRepository<DiscInBag, Long> {

    void deleteByUserDisc_UserDiscIdAndBag_Id(Long userDiscId, Long bagId);

    boolean existsByUserDisc_UserDiscId(Long userDiscId);

    boolean existsByUserDisc_UserDiscIdAndBag_Id(Long userDIscId, Long bagId);
}
