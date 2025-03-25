package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.models.Bag;
import com.discgolf.in_the_bag.models.DiscInBag;
import com.discgolf.in_the_bag.models.UserDisc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiscInBagRepository extends JpaRepository<DiscInBag, Long> {

    void deleteByUserDisc_UserDiscIdAndBag_Id(Long userDiscId, Long bagId);

    boolean existsByUserDisc_UserDiscId(Long userDiscId);

    boolean existsByUserDisc_UserDiscIdAndBag_Id(Long userDIscId, Long bagId);

    void deleteByUserDiscAndBag(UserDisc userDisc, Bag bag);

    // Finds all UserDisc Ids for a certain bag
    @Query("SELECT dib.userDisc.userDiscId FROM DiscInBag dib WHERE dib.bag.id = :bagId")
    List<Long> findUserDiscIdsByBagId(@Param("bagId") Long bagId);

}
