package com.sports.dto.response;

import com.sports.model.enums.GameLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceSummaryResponse {
    private Long id;
    private Long sportId;
    private String sportName;
    private GameLevel level;
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int draws;
    private int totalScore;
    private Double averageScore;
    private Double winPercentage;
    private boolean advancedToNextLevel;
    private LocalDateTime completedAt;
}
