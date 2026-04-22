package com.sports.service;

import com.sports.model.enums.MatchResult;
import com.sports.model.enums.SportScoreType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MatchScoreEvaluator {

    public ScoreEvaluationResult evaluate(SportScoreType type, Map<String, Object> d) {
        if (d == null || d.isEmpty()) return new ScoreEvaluationResult(0, 0, MatchResult.DRAW);

        return switch (type) {
            case CRICKET           -> evaluateCricket(d);
            case KABADDI           -> evaluateKabaddi(d);
            case TIME_LOWER_WINS   -> evaluateTimeLowerWins(d);
            case DISTANCE_HIGHER_WINS -> evaluateDistanceHigherWins(d);
            case COMBAT            -> evaluateCombat(d);
            case ARTISTIC_SCORE    -> evaluateArtistic(d);
            case WEIGHTLIFTING     -> evaluateWeightlifting(d);
            case GOALS             -> evaluateGoals(d);
            case SETS              -> evaluateSets(d);
            case ACCURACY          -> evaluateAccuracy(d);
            default                -> evaluateGenericPoints(d);
        };
    }

    // ── Sport-specific evaluators ────────────────────────────────────────────

    private ScoreEvaluationResult evaluateCricket(Map<String, Object> d) {
        int playerRuns   = getInt(d, "playerRuns");
        int oppRuns      = getInt(d, "opponentRuns");
        return new ScoreEvaluationResult(playerRuns, oppRuns, compareHigher(playerRuns, oppRuns));
    }

    private ScoreEvaluationResult evaluateKabaddi(Map<String, Object> d) {
        int p = getInt(d, "playerRaidPoints") + getInt(d, "playerTacklePoints") + getInt(d, "playerBonusPoints");
        int o = getInt(d, "opponentRaidPoints") + getInt(d, "opponentTacklePoints") + getInt(d, "opponentBonusPoints");
        return new ScoreEvaluationResult(p, o, compareHigher(p, o));
    }

    /** Lower time = WIN. Stored as time * 10 (one decimal preserved as int). */
    private ScoreEvaluationResult evaluateTimeLowerWins(Map<String, Object> d) {
        double pt = getDouble(d, "playerTimeSeconds");
        double ot = getDouble(d, "opponentTimeSeconds");
        MatchResult result = (pt < ot) ? MatchResult.WIN : (pt > ot) ? MatchResult.LOSS : MatchResult.DRAW;
        return new ScoreEvaluationResult((int)(pt * 10), (int)(ot * 10), result);
    }

    /** Farther distance = WIN. Stored in cm. */
    private ScoreEvaluationResult evaluateDistanceHigherWins(Map<String, Object> d) {
        int p = (int) getDouble(d, "playerDistanceCm");
        int o = (int) getDouble(d, "opponentDistanceCm");
        return new ScoreEvaluationResult(p, o, compareHigher(p, o));
    }

    private ScoreEvaluationResult evaluateCombat(Map<String, Object> d) {
        boolean ko = getBool(d, "endedByKO") || getBool(d, "endedByIppon");
        if (ko) return new ScoreEvaluationResult(10, 0, MatchResult.WIN);
        int p = getInt(d, "playerPoints");
        int o = getInt(d, "opponentPoints");
        return new ScoreEvaluationResult(p, o, compareHigher(p, o));
    }

    /** Artistic / gymnastics / diving scores stored as score * 10. */
    private ScoreEvaluationResult evaluateArtistic(Map<String, Object> d) {
        double p = getDouble(d, "playerScore");
        double o = getDouble(d, "opponentScore");
        MatchResult result = (p > o) ? MatchResult.WIN : (p < o) ? MatchResult.LOSS : MatchResult.DRAW;
        return new ScoreEvaluationResult((int)(p * 10), (int)(o * 10), result);
    }

    private ScoreEvaluationResult evaluateWeightlifting(Map<String, Object> d) {
        double p = getDouble(d, "playerSnatch") + getDouble(d, "playerCleanJerk");
        double o = getDouble(d, "opponentSnatch") + getDouble(d, "opponentCleanJerk");
        MatchResult result = (p > o) ? MatchResult.WIN : (p < o) ? MatchResult.LOSS : MatchResult.DRAW;
        return new ScoreEvaluationResult((int) p, (int) o, result);
    }

    private ScoreEvaluationResult evaluateGoals(Map<String, Object> d) {
        int p = getInt(d, "playerGoals");
        int o = getInt(d, "opponentGoals");
        return new ScoreEvaluationResult(p, o, compareHigher(p, o));
    }

    private ScoreEvaluationResult evaluateSets(Map<String, Object> d) {
        int p = getInt(d, "playerSets");
        int o = getInt(d, "opponentSets");
        return new ScoreEvaluationResult(p, o, compareHigher(p, o));
    }

    private ScoreEvaluationResult evaluateAccuracy(Map<String, Object> d) {
        int p = getInt(d, "playerScore");
        int o = getInt(d, "opponentScore");
        return new ScoreEvaluationResult(p, o, compareHigher(p, o));
    }

    private ScoreEvaluationResult evaluateGenericPoints(Map<String, Object> d) {
        int p = 0, o = 0;
        for (String k : new String[]{"playerPoints", "playerGoals", "playerSets", "playerScore"}) {
            if (d.containsKey(k)) { p = getInt(d, k); break; }
        }
        for (String k : new String[]{"opponentPoints", "opponentGoals", "opponentSets", "opponentScore"}) {
            if (d.containsKey(k)) { o = getInt(d, k); break; }
        }
        return new ScoreEvaluationResult(p, o, compareHigher(p, o));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private MatchResult compareHigher(int p, int o) {
        if (p > o) return MatchResult.WIN;
        if (p < o) return MatchResult.LOSS;
        return MatchResult.DRAW;
    }

    private int getInt(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return 0;
        if (v instanceof Number n) return n.intValue();
        try { return Integer.parseInt(v.toString().trim()); } catch (Exception e) { return 0; }
    }

    private double getDouble(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return 0.0;
        if (v instanceof Number n) return n.doubleValue();
        try { return Double.parseDouble(v.toString().trim()); } catch (Exception e) { return 0.0; }
    }

    private boolean getBool(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return false;
        if (v instanceof Boolean b) return b;
        return "true".equalsIgnoreCase(v.toString().trim());
    }
}
