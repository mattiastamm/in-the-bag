package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.records.BagRecord;
import com.discgolf.in_the_bag.models.Bag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BagRepository extends JpaRepository<Bag, Long> {

    @Query("""
        SELECT new com.discgolf.in_the_bag.records.BagRecord(
            b.id, b.title, b.comment
        )
        FROM UserDisc ud
        JOIN ud.bags b
        WHERE ud.id = :discId
    """)
    List<BagRecord> findBagsByDiscId(@Param("discId") Long discId);
}
