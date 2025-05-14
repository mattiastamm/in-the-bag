package com.discgolf.in_the_bag.repositories;

import com.discgolf.in_the_bag.models.Bag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BagRepository extends JpaRepository<Bag, Long> {

    List<Bag> findByUserId(Long userId);
    @Transactional
    void deleteById(Long bagId);

}
