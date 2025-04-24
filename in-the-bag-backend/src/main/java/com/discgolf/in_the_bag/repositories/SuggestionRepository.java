package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.models.Suggestion;
import com.discgolf.in_the_bag.suggestions.DiscCategory;
import com.discgolf.in_the_bag.suggestions.DiscSuggestionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    @Query("SELECT s.id FROM Suggestion s")
    Set<Long> findAllIds();


    @Query("""
    SELECT new com.discgolf.in_the_bag.suggestions.DiscSuggestionDto(
        s.id,
        s.disc.name,
        s.disc.manufacturer.name,
        s.disc.speed,
        s.disc.glide,
        s.disc.turn,
        s.disc.fade
    )
    FROM Suggestion s
    WHERE s.discCategory = :discCategory
""")
    List<DiscSuggestionDto> findDiscSuggestionDtosByDiscCategory(@Param("discCategory") DiscCategory discCategory);

}
