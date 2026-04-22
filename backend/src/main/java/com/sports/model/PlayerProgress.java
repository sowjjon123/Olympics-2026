package com.sports.model;

import com.sports.model.enums.GameLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "player_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "sport_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_level", nullable = false)
    private GameLevel currentLevel = GameLevel.LEVEL_1;

    @Column(name = "matches_played_in_level", nullable = false)
    private int matchesPlayedInLevel = 0;

    @Column(name = "wins_in_level", nullable = false)
    private int winsInLevel = 0;

    @Column(name = "total_matches", nullable = false)
    private int totalMatches = 0;

    @Column(name = "total_wins", nullable = false)
    private int totalWins = 0;

    @Column(name = "total_losses", nullable = false)
    private int totalLosses = 0;

    @Column(name = "total_draws", nullable = false)
    private int totalDraws = 0;

    @Column(name = "total_score", nullable = false)
    private int totalScore = 0;

    @Column(name = "qualified_for_district", nullable = false)
    private boolean qualifiedForDistrict = false;

    @Column(name = "qualified_for_state", nullable = false)
    private boolean qualifiedForState = false;

    @Column(name = "qualified_for_olympics", nullable = false)
    private boolean qualifiedForOlympics = false;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
