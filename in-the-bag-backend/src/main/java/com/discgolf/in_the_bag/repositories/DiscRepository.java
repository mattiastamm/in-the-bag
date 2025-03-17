package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.models.Disc;
import com.discgolf.in_the_bag.records.DiscAutoFillBaseRecord;
import com.discgolf.in_the_bag.records.DiscAutoFillRecord;
import com.discgolf.in_the_bag.records.DiscSearchRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscRepository extends JpaRepository<Disc, Long> {

    @Query("""
        SELECT new com.discgolf.in_the_bag.records.DiscSearchRecord(d.id, d.name)
        FROM Disc d
        WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<DiscSearchRecord> searchDiscsByName(@Param("query") String query);

    @Query("""
        SELECT new com.discgolf.in_the_bag.records.DiscAutoFillBaseRecord(
            d.id,
            d.name,
            d.type,
            d.manufacturer.id,
            d.manufacturer.name,
            d.speed,
            d.glide,
            d.turn,
            d.fade
        )
        FROM Disc d
        WHERE d.id = :discId
    """)
    Optional<DiscAutoFillBaseRecord> findDiscDetailsForCreation(@Param("discId") Long discId);


}

