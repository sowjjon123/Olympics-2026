package com.sports.service;

import com.sports.dto.response.PerformanceSummaryResponse;
import com.sports.dto.response.PlayerProgressResponse;
import com.sports.exception.ResourceNotFoundException;
import com.sports.model.PlayerProgress;
import com.sports.model.Sport;
import com.sports.repository.PerformanceSummaryRepository;
import com.sports.repository.PlayerProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PerformanceService {

    private final PlayerProgressRepository playerProgressRepository;
    private final PerformanceSummaryRepository performanceSummaryRepository;
    private final SportService sportService;

    public PerformanceService(PlayerProgressRepository playerProgressRepository,
                              PerformanceSummaryRepository performanceSummaryRepository,
                              SportService sportService) {
        this.playerProgressRepository = playerProgressRepository;
        this.performanceSummaryRepository = performanceSummaryRepository;
        this.sportService = sportService;
    }

    public PlayerProgressResponse getProgressBySport(Long userId, Long sportId) {
        Sport sport = sportService.findById(sportId);
        PlayerProgress progress = playerProgressRepository
                .findByUserIdAndSportId(userId, sportId)
                .orElseThrow(() -> new ResourceNotFoundException("No progress found for this sport. Start playing matches first!"));
        return mapProgressToResponse(progress, sport);
    }

    public List<PlayerProgressResponse> getAllProgressForUser(Long userId) {
        return playerProgressRepository.findByUserId(userId)
                .stream()
                .map(p -> mapProgressToResponse(p, p.getSport()))
                .collect(Collectors.toList());
    }

    public List<PerformanceSummaryResponse> getPerformanceSummary(Long userId, Long sportId) {
        sportService.findById(sportId);
        return performanceSummaryRepository
                .findByUserIdAndSportIdOrderByLevelAsc(userId, sportId)
                .stream()
                .map(s -> PerformanceSummaryResponse.builder()
                        .id(s.getId())
                        .sportId(s.getSport().getId())
                        .sportName(s.getSport().getName())
                        .level(s.getLevel())
                        .matchesPlayed(s.getMatchesPlayed())
                        .wins(s.getWins())
                        .losses(s.getLosses())
                        .draws(s.getDraws())
                        .totalScore(s.getTotalScore())
                        .averageScore(s.getAverageScore())
                        .winPercentage(s.getWinPercentage())
                        .advancedToNextLevel(s.isAdvancedToNextLevel())
                        .completedAt(s.getCompletedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<PerformanceSummaryResponse> getAllPerformanceForUser(Long userId) {
        return performanceSummaryRepository.findByUserId(userId)
                .stream()
                .map(s -> PerformanceSummaryResponse.builder()
                        .id(s.getId())
                        .sportId(s.getSport().getId())
                        .sportName(s.getSport().getName())
                        .level(s.getLevel())
                        .matchesPlayed(s.getMatchesPlayed())
                        .wins(s.getWins())
                        .losses(s.getLosses())
                        .draws(s.getDraws())
                        .totalScore(s.getTotalScore())
                        .averageScore(s.getAverageScore())
                        .winPercentage(s.getWinPercentage())
                        .advancedToNextLevel(s.isAdvancedToNextLevel())
                        .completedAt(s.getCompletedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private PlayerProgressResponse mapProgressToResponse(PlayerProgress p, Sport sport) {
        int matchesRemaining = sport.getMatchesPerLevel() - p.getMatchesPlayedInLevel();
        int winsNeeded = Math.max(0, sport.getWinsRequiredToAdvance() - p.getWinsInLevel());
        double winRate = p.getTotalMatches() > 0
                ? (double) p.getTotalWins() / p.getTotalMatches() * 100
                : 0.0;

        return PlayerProgressResponse.builder()
                .id(p.getId())
                .sportId(sport.getId())
                .sportName(sport.getName())
                .currentLevel(p.getCurrentLevel())
                .matchesPlayedInLevel(p.getMatchesPlayedInLevel())
                .winsInLevel(p.getWinsInLevel())
                .totalMatches(p.getTotalMatches())
                .totalWins(p.getTotalWins())
                .totalLosses(p.getTotalLosses())
                .totalDraws(p.getTotalDraws())
                .winRate(Math.round(winRate * 100.0) / 100.0)
                .qualifiedForDistrict(p.isQualifiedForDistrict())
                .qualifiedForState(p.isQualifiedForState())
                .qualifiedForOlympics(p.isQualifiedForOlympics())
                .matchesRemainingInLevel(matchesRemaining)
                .winsNeededToAdvance(winsNeeded)
                .build();
    }
}
