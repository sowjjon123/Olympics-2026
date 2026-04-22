package com.sports.model;

import com.sports.model.enums.GameLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "performance_summary",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "sport_id", "level"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceSummary {

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
    @Column(nullable = false)
    private GameLevel level;

    @Column(name = "matches_played", nullable = false)
    private int matchesPlayed = 0;

    @Column(name = "wins", nullable = false)
    private int wins = 0;

    @Column(name = "losses", nullable = false)
    private int losses = 0;

    @Column(name = "draws", nullable = false)
    private int draws = 0;

    @Column(name = "total_score", nullable = false)
    private int totalScore = 0;

    @Column(name = "average_score")
    private Double averageScore = 0.0;

    @Column(name = "win_percentage")
    private Double winPercentage = 0.0;

    @Column(name = "advanced_to_next_level", nullable = false)
    private boolean advancedToNextLevel = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
