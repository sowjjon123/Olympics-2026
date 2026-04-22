package com.sports.controller;

import com.sports.dto.request.MatchResultRequest;
import com.sports.dto.response.ApiResponse;
import com.sports.dto.response.MatchResponse;
import com.sports.dto.response.PlayerProgressResponse;
import com.sports.model.PlayerProgress;
import com.sports.repository.UserRepository;
import com.sports.service.MatchService;
import com.sports.service.PerformanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;
    private final PerformanceService performanceService;
    private final UserRepository userRepository;

    public MatchController(MatchService matchService,
                           PerformanceService performanceService,
                           UserRepository userRepository) {
        this.matchService = matchService;
        this.performanceService = performanceService;
        this.userRepository = userRepository;
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<MatchResponse>> submitMatchResult(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody MatchResultRequest request) {
        Long userId = resolveUserId(userDetails);
        MatchResponse response = matchService.submitMatchResult(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Match result recorded", response));
    }

    @PostMapping("/advance/{sportId}")
    public ResponseEntity<ApiResponse<PlayerProgressResponse>> advanceToNextLevel(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long sportId) {
        Long userId = resolveUserId(userDetails);
        matchService.advanceToNextLevel(userId, sportId);
        PlayerProgressResponse progress = performanceService.getProgressBySport(userId, sportId);
        return ResponseEntity.ok(ApiResponse.success("Advanced to next level: " + progress.getCurrentLevel(), progress));
    }

    @GetMapping("/history/{sportId}")
    public ResponseEntity<ApiResponse<List<MatchResponse>>> getMatchHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long sportId) {
        Long userId = resolveUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success("Match history", matchService.getMatchHistory(userId, sportId)));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<MatchResponse>>> getRecentMatches(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = resolveUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success("Recent matches", matchService.getRecentMatches(userId)));
    }

    private Long resolveUserId(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow()
                .getId();
    }
}
