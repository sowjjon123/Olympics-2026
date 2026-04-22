package com.sports.repository;

import com.sports.model.PlayerProgress;
import com.sports.model.enums.GameLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerProgressRepository extends JpaRepository<PlayerProgress, Long> {

    Optional<PlayerProgress> findByUserIdAndSportId(Long userId, Long sportId);

    List<PlayerProgress> findByUserId(Long userId);

    List<PlayerProgress> findByUserIdAndCurrentLevel(Long userId, GameLevel level);

    List<PlayerProgress> findByQualifiedForOlympicsTrue();

    List<PlayerProgress> findBySportIdAndQualifiedForDistrictTrue(Long sportId);

    List<PlayerProgress> findBySportIdAndQualifiedForStateTrue(Long sportId);
}
