package com.sports.repository;

import com.sports.model.PerformanceSummary;
import com.sports.model.enums.GameLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceSummaryRepository extends JpaRepository<PerformanceSummary, Long> {

    List<PerformanceSummary> findByUserIdAndSportIdOrderByLevelAsc(Long userId, Long sportId);

    Optional<PerformanceSummary> findByUserIdAndSportIdAndLevel(Long userId, Long sportId, GameLevel level);

    List<PerformanceSummary> findByUserId(Long userId);

    List<PerformanceSummary> findBySportIdAndLevel(Long sportId, GameLevel level);
}
