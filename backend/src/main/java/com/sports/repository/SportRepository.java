package com.sports.repository;

import com.sports.model.Sport;
import com.sports.model.enums.SportCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {
    List<Sport> findByOlympicSport(boolean olympicSport);
    List<Sport> findByCategory(SportCategory category);
    boolean existsByName(String name);
}
