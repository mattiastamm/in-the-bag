package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.models.WishlistEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntry, Long> {

    List<WishlistEntry> findByUserId(Long userId);

    boolean existsByUserIdAndDisc_Id(Long userId, Long discId);

    void deleteByUserIdAndDisc_Id(Long userId, Long discId);

    @Query("SELECT w.disc.id FROM WishlistEntry w WHERE w.userId = :userId")
    List<Long> findDiscIdsByUserId(@Param("userId") Long userId);

}

