package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.models.Plastic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlasticRepository extends JpaRepository<Plastic, Long> {

    Optional<Plastic> findPlasticEntityById(Long id);


}
