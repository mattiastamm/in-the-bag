package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.models.Plastic;
import com.discgolf.in_the_bag.records.PlasticRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlasticRepository extends JpaRepository<Plastic, Long> {

    Optional<Plastic> findPlasticEntityById(Long id);

    @Query("""
    SELECT new com.discgolf.in_the_bag.records.PlasticRecord(
        p.id,
        p.name
    )
    FROM Plastic p
    WHERE p.manufacturer.id = :manufacturerId
""")
    List<PlasticRecord> findPlasticsByManufacturer(@Param("manufacturerId") Integer manufacturerId);

}
