package com.sports.repository;

import com.sports.model.Match;
import com.sports.model.enums.GameLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByUserIdAndSportIdOrderByPlayedAtDesc(Long userId, Long sportId);

    List<Match> findByUserIdAndSportIdAndLevelOrderByPlayedAtAsc(Long userId, Long sportId, GameLevel level);

    @Query("SELECT COUNT(m) FROM Match m WHERE m.user.id = :userId AND m.sport.id = :sportId AND m.level = :level")
    int countMatchesByUserSportAndLevel(@Param("userId") Long userId,
                                        @Param("sportId") Long sportId,
                                        @Param("level") GameLevel level);

    @Query("SELECT COUNT(m) FROM Match m WHERE m.user.id = :userId AND m.sport.id = :sportId AND m.level = :level AND m.result = 'WIN'")
    int countWinsByUserSportAndLevel(@Param("userId") Long userId,
                                     @Param("sportId") Long sportId,
                                     @Param("level") GameLevel level);

    @Query("SELECT m FROM Match m WHERE m.user.id = :userId ORDER BY m.playedAt DESC")
    List<Match> findRecentMatchesByUser(@Param("userId") Long userId);
}
