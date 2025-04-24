package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.models.Wishlist;
import com.discgolf.in_the_bag.records.WishlistDiscDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {


    boolean existsByUserIdAndSuggestionId(Long userId, Long suggestionId);

    @Transactional
    void deleteByUserIdAndSuggestionId(Long userId, Long suggestionId);

    @Query("""
    SELECT w.suggestion.id
    FROM Wishlist w
    WHERE w.user.id = :userId
""")
    Set<Long> findAllSuggestionIdsByUserId(@Param("userId") Long userId);


    @Query("""
    SELECT new com.discgolf.in_the_bag.records.WishlistDiscDto(
        d.id,
        s.id,
        d.name,
        m.name,
        d.speed,
        d.glide,
        d.turn,
        d.fade,
        s.category,
        s.stability
    )
    FROM Wishlist w
    LEFT JOIN w.suggestion s
    LEFT JOIN s.disc d
    LEFT JOIN d.manufacturer m
    WHERE w.user.id = :userId
""")
    List<WishlistDiscDto> findWishlistDiscsByUserId(@Param("userId") Long userId);

}

