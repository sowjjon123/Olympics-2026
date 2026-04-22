package com.sports.service;

import com.sports.dto.request.MatchResultRequest;
import com.sports.dto.response.MatchResponse;
import com.sports.exception.BadRequestException;
import com.sports.exception.ResourceNotFoundException;
import com.sports.model.*;
import com.sports.model.enums.GameLevel;
import com.sports.model.enums.MatchResult;
import com.sports.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private static final List<GameLevel> LEVEL_PROGRESSION = Arrays.asList(
            GameLevel.LEVEL_1,
            GameLevel.LEVEL_2,
            GameLevel.LEVEL_3,
            GameLevel.LEVEL_4,
            GameLevel.LEVEL_5,
            GameLevel.DISTRICT,
            GameLevel.STATE,
            GameLevel.OLYMPICS
    );

    private final MatchRepository matchRepository;
    private final PlayerProgressRepository playerProgressRepository;
    private final PerformanceSummaryRepository performanceSummaryRepository;
    private final UserRepository userRepository;
    private final SportService sportService;
    private final MatchScoreEvaluator scoreEvaluator;

    public MatchService(MatchRepository matchRepository,
                        PlayerProgressRepository playerProgressRepository,
                        PerformanceSummaryRepository performanceSummaryRepository,
                        UserRepository userRepository,
                        SportService sportService,
                        MatchScoreEvaluator scoreEvaluator) {
        this.matchRepository = matchRepository;
        this.playerProgressRepository = playerProgressRepository;
        this.performanceSummaryRepository = performanceSummaryRepository;
        this.userRepository = userRepository;
        this.sportService = sportService;
        this.scoreEvaluator = scoreEvaluator;
    }

    @Transactional
    public MatchResponse submitMatchResult(Long userId, MatchResultRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Sport sport = sportService.findById(request.getSportId());

        PlayerProgress progress = playerProgressRepository
                .findByUserIdAndSportId(userId, sport.getId())
                .orElseGet(() -> createNewProgress(user, sport));

        if (isLevelCompleted(progress, sport)) {
            throw new BadRequestException(
                    "All matches for level " + progress.getCurrentLevel() +
                    " are complete. Please advance to the next level.");
        }

        // Sport-specific score evaluation — takes priority over raw scores
        Map<String, Object> scoreDetails = request.getScoreDetails();
        int playerScore;
        int opponentScore;
        MatchResult result;

        if (scoreDetails != null && !scoreDetails.isEmpty() && sport.getScoreType() != null) {
            ScoreEvaluationResult eval = scoreEvaluator.evaluate(sport.getScoreType(), scoreDetails);
            playerScore   = eval.playerScore();
            opponentScore = eval.opponentScore();
            result        = eval.result();
        } else {
            playerScore   = request.getPlayerScore() != null ? request.getPlayerScore() : 0;
            opponentScore = request.getOpponentScore() != null ? request.getOpponentScore() : 0;
            result        = determineResult(playerScore, opponentScore);
        }

        Match match = Match.builder()
                .user(user)
                .sport(sport)
                .level(progress.getCurrentLevel())
                .playerScore(playerScore)
                .opponentScore(opponentScore)
                .result(result)
                .matchNumberInLevel(progress.getMatchesPlayedInLevel() + 1)
                .matchNotes(request.getMatchNotes())
                .scoreDetails(scoreDetails)
                .build();

        match = matchRepository.save(match);
        updateProgress(progress, result, playerScore, sport);

        return mapToMatchResponse(match, sport);
    }

    @Transactional
    public PlayerProgress advanceToNextLevel(Long userId, Long sportId) {
        PlayerProgress progress = playerProgressRepository
                .findByUserIdAndSportId(userId, sportId)
                .orElseThrow(() -> new ResourceNotFoundException("No progress found for this sport"));

        Sport sport = sportService.findById(sportId);

        if (!isLevelCompleted(progress, sport)) {
            throw new BadRequestException("You have not completed all matches in the current level yet.");
        }

        if (progress.getWinsInLevel() < sport.getWinsRequiredToAdvance()) {
            throw new BadRequestException(
                    "You need " + sport.getWinsRequiredToAdvance() + " wins to advance. " +
                    "You have " + progress.getWinsInLevel() + " wins.");
        }

        savePerformanceSummary(progress, sport, true);
        progressToNextLevel(progress);

        return playerProgressRepository.save(progress);
    }

    public List<MatchResponse> getMatchHistory(Long userId, Long sportId) {
        return matchRepository.findByUserIdAndSportIdOrderByPlayedAtDesc(userId, sportId)
                .stream()
                .map(m -> mapToMatchResponse(m, m.getSport()))
                .collect(Collectors.toList());
    }

    public List<MatchResponse> getRecentMatches(Long userId) {
        return matchRepository.findRecentMatchesByUser(userId)
                .stream()
                .map(m -> mapToMatchResponse(m, m.getSport()))
                .collect(Collectors.toList());
    }

    // -------------------- Private Helpers --------------------

    private PlayerProgress createNewProgress(User user, Sport sport) {
        PlayerProgress progress = PlayerProgress.builder()
                .user(user)
                .sport(sport)
                .currentLevel(GameLevel.LEVEL_1)
                .matchesPlayedInLevel(0)
                .winsInLevel(0)
                .build();
        return playerProgressRepository.save(progress);
    }

    private boolean isLevelCompleted(PlayerProgress progress, Sport sport) {
        return progress.getMatchesPlayedInLevel() >= sport.getMatchesPerLevel();
    }

    private MatchResult determineResult(int playerScore, int opponentScore) {
        if (playerScore > opponentScore) return MatchResult.WIN;
        if (playerScore < opponentScore) return MatchResult.LOSS;
        return MatchResult.DRAW;
    }

    private void updateProgress(PlayerProgress progress, MatchResult result, int playerScore, Sport sport) {
        progress.setMatchesPlayedInLevel(progress.getMatchesPlayedInLevel() + 1);
        progress.setTotalMatches(progress.getTotalMatches() + 1);
        progress.setTotalScore(progress.getTotalScore() + playerScore);

        switch (result) {
            case WIN -> {
                progress.setWinsInLevel(progress.getWinsInLevel() + 1);
                progress.setTotalWins(progress.getTotalWins() + 1);
            }
            case LOSS -> progress.setTotalLosses(progress.getTotalLosses() + 1);
            case DRAW -> progress.setTotalDraws(progress.getTotalDraws() + 1);
            default -> { }
        }

        playerProgressRepository.save(progress);
    }

    private void progressToNextLevel(PlayerProgress progress) {
        int currentIndex = LEVEL_PROGRESSION.indexOf(progress.getCurrentLevel());

        if (currentIndex < LEVEL_PROGRESSION.size() - 1) {
            GameLevel nextLevel = LEVEL_PROGRESSION.get(currentIndex + 1);
            progress.setCurrentLevel(nextLevel);
            progress.setMatchesPlayedInLevel(0);
            progress.setWinsInLevel(0);

            if (nextLevel == GameLevel.DISTRICT) progress.setQualifiedForDistrict(true);
            if (nextLevel == GameLevel.STATE) progress.setQualifiedForState(true);
            if (nextLevel == GameLevel.OLYMPICS) progress.setQualifiedForOlympics(true);
        }
    }

    private void savePerformanceSummary(PlayerProgress progress, Sport sport, boolean advanced) {
        int played = progress.getMatchesPlayedInLevel();
        int wins = progress.getWinsInLevel();
        int losses = progress.getTotalLosses();
        double avgScore = played > 0 ? (double) progress.getTotalScore() / progress.getTotalMatches() : 0.0;
        double winPct = played > 0 ? (double) wins / played * 100 : 0.0;

        PerformanceSummary summary = performanceSummaryRepository
                .findByUserIdAndSportIdAndLevel(progress.getUser().getId(), sport.getId(), progress.getCurrentLevel())
                .orElse(PerformanceSummary.builder()
                        .user(progress.getUser())
                        .sport(sport)
                        .level(progress.getCurrentLevel())
                        .build());

        summary.setMatchesPlayed(played);
        summary.setWins(wins);
        summary.setLosses(progress.getTotalLosses());
        summary.setDraws(progress.getTotalDraws());
        summary.setTotalScore(progress.getTotalScore());
        summary.setAverageScore(avgScore);
        summary.setWinPercentage(winPct);
        summary.setAdvancedToNextLevel(advanced);
        summary.setCompletedAt(LocalDateTime.now());

        performanceSummaryRepository.save(summary);
    }

    private MatchResponse mapToMatchResponse(Match match, Sport sport) {
        return MatchResponse.builder()
                .id(match.getId())
                .sportId(sport.getId())
                .sportName(sport.getName())
                .level(match.getLevel())
                .playerScore(match.getPlayerScore())
                .opponentScore(match.getOpponentScore())
                .result(match.getResult())
                .matchNumberInLevel(match.getMatchNumberInLevel())
                .playedAt(match.getPlayedAt())
                .matchNotes(match.getMatchNotes())
                .scoreDetails(match.getScoreDetails())
                .build();
    }
}
