package com.sports.dto.response;

import com.sports.model.enums.GameLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerProgressResponse {
    private Long id;
    private Long sportId;
    private String sportName;
    private GameLevel currentLevel;
    private int matchesPlayedInLevel;
    private int winsInLevel;
    private int totalMatches;
    private int totalWins;
    private int totalLosses;
    private int totalDraws;
    private double winRate;
    private boolean qualifiedForDistrict;
    private boolean qualifiedForState;
    private boolean qualifiedForOlympics;
    private int matchesRemainingInLevel;
    private int winsNeededToAdvance;
}
