package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.BaseDiscDetailsRecord;
import com.discgolf.in_the_bag.records.InventoryDiscRecord;
import com.discgolf.in_the_bag.records.PlasticRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscRepository extends JpaRepository<UserDisc, Long> {

    // ✅ Use this for Inventory View (custom DTO projection)
    @Query("""
        SELECT new com.discgolf.in_the_bag.records.InventoryDiscRecord(
            ud.userDiscId, ud.disc.name, ud.disc.type, 
            ud.customSpeed, ud.customGlide, ud.customTurn, ud.customFade, 
            ud.color, ud.plastic.name,  
            ud.disc.manufacturer.name, 
            ud.disc.speed, ud.disc.glide, ud.disc.turn, ud.disc.fade, 
            ud.inUse)
        FROM UserDisc ud
        WHERE ud.userId = :userId
    """)
    List<InventoryDiscRecord> findDiscsByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT new com.discgolf.in_the_bag.records.BaseDiscDetailsRecord(
            ud.userDiscId,
            ud.disc.name,
            ud.disc.type,
            ud.customSpeed,
            ud.customGlide,
            ud.customTurn,
            ud.customFade,
            ud.color,
            ud.plastic.id,
            ud.plastic.name,
            ud.disc.manufacturer.name,
            ud.disc.speed,
            ud.disc.glide,
            ud.disc.turn,
            ud.disc.fade,
            ud.weight,
            ud.inUse,
            ud.comment
        )
        FROM UserDisc ud
        WHERE ud.userDiscId = :userDiscId
    """)
    Optional<BaseDiscDetailsRecord> findBaseDiscDetailsById(@Param("userDiscId") Long userDiscId);

    @Query("""
    SELECT new com.discgolf.in_the_bag.records.PlasticRecord(p.id, p.name)
    FROM Plastic p
    WHERE p.manufacturer.id = (
        SELECT ud.disc.manufacturer.id FROM UserDisc ud WHERE ud.userDiscId = :userDiscId
    )
""")
    List<PlasticRecord> findPlasticsByUserDiscId(@Param("userDiscId") Long userDiscId);


    // ✅ Use these for modifying/deleting UserDiscs (they return real entities)
    List<UserDisc> findDiscEntitiesByUserId(Long userId);
    Optional<UserDisc> findDiscEntityByUserDiscId(Long userDiscId);
}
