package com.sports.service;

import com.sports.model.enums.MatchResult;

public record ScoreEvaluationResult(int playerScore, int opponentScore, MatchResult result) {}
