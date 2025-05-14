package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.models.Bag;
import com.discgolf.in_the_bag.models.DiscInBag;
import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.BagRecord;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiscInBagRepository extends JpaRepository<DiscInBag, Long> {

    boolean existsByUserDisc_Id(Long userDiscId);

    boolean existsByUserDisc_IdAndBag_Id(Long userDIscId, Long bagId);

    void deleteByUserDiscAndBag(UserDisc userDisc, Bag bag);
    void deleteByUserDisc_IdAndBag_Id(Long userDiscId, Long bagId);

    @Transactional
    void deleteByBag_Id(Long bagId);

    @Query("SELECT dib.userDisc.id FROM DiscInBag dib WHERE dib.bag.id = :bagId")
    List<Long> findUserDiscIdsByBagId(@Param("bagId") Long bagId);

    @Query("SELECT dib.userDisc FROM DiscInBag dib WHERE dib.bag.id = :bagId")
    List<UserDisc> findUserDiscsByBagId(@Param("bagId") Long bagId);

    @Query("""
    SELECT new com.discgolf.in_the_bag.records.BagRecord(
        dib.bag.id, dib.bag.title, dib.bag.comment
    )
    FROM DiscInBag dib
    WHERE dib.userDisc.id = :userDiscId
""")
    List<BagRecord> findBagsByUserDiscId(@Param("userDiscId") Long userDiscId);

}
