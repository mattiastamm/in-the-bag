package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.records.BagRecord;
import com.discgolf.in_the_bag.models.Bag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BagRepository extends JpaRepository<Bag, Long> {

    List<Bag> findByUserId(Long userId);
    @Transactional
    void deleteById(Long bagId);

    @Query("""
        SELECT new com.discgolf.in_the_bag.records.BagRecord(
            b.id, b.title, b.comment
        )
        FROM UserDisc ud
        JOIN ud.bags b
        WHERE ud.userDiscId = :userDiscId
    """)
    List<BagRecord> findBagsByUserDiscId(@Param("userDiscId") Long userDiscId);

}
