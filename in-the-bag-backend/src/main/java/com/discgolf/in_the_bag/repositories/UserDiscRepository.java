package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.PlasticRecord;
import com.discgolf.in_the_bag.records.UserDiscDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDiscRepository extends JpaRepository<UserDisc, Long> {

    List<UserDisc> findAllByUserId(Long userId);

    void deleteById(Long UserDiscId);

    // ✅ Use this for Inventory View (custom DTO projection)
    @Query("""
    SELECT new com.discgolf.in_the_bag.records.UserDiscDto(
        ud.id,
        d.name,
        d.type,
        ud.customSpeed,
        ud.customGlide,
        ud.customTurn,
        ud.customFade,
        ud.color,
        p.id,
        p.name,
        ud.customPlastic,
        m.name,
        d.speed,
        d.glide,
        d.turn,
        d.fade,
        ud.weight,
        ud.inUse,
        ud.comment
    )
    FROM UserDisc ud
    JOIN ud.disc d
    JOIN d.manufacturer m
    LEFT JOIN ud.plastic p
    WHERE ud.user.id = :userId
""")
    List<UserDiscDto> findUserDiscsByUserId(@Param("userId") Long userId);

    @Query("""
    SELECT new com.discgolf.in_the_bag.records.UserDiscDto(
        ud.id,
        d.name,
        d.type,
        ud.customSpeed,
        ud.customGlide,
        ud.customTurn,
        ud.customFade,
        ud.color,
        p.id,
        p.name,
        ud.customPlastic,
        m.name,
        d.speed,
        d.glide,
        d.turn,
        d.fade,
        ud.weight,
        ud.inUse,
        ud.comment
    )
    FROM UserDisc ud
    JOIN ud.disc d
    JOIN d.manufacturer m
    LEFT JOIN ud.plastic p
    WHERE ud.id = :userDiscId
""")
    Optional<UserDiscDto> findUserDiscById(@Param("userDiscId") Long userDiscId);


    @Query("""
    SELECT new com.discgolf.in_the_bag.records.PlasticRecord(p.id, p.name)
    FROM Plastic p
    WHERE p.manufacturer.id = (
        SELECT ud.disc.manufacturer.id FROM UserDisc ud WHERE ud.id = :userDiscId
    )
""")
    List<PlasticRecord> findPlasticsByUserDiscId(@Param("userDiscId") Long userDiscId);

    @Modifying
    @Query("UPDATE UserDisc ud SET ud.inUse = :stillInUse WHERE ud.id = :userDiscId")
    void updateInUseStatus(@Param("userDiscId") Long userDiscId, @Param("stillInUse") boolean stillInUse);

}
