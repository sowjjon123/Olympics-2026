package com.sports.controller;

import com.sports.dto.response.ApiResponse;
import com.sports.dto.response.PerformanceSummaryResponse;
import com.sports.dto.response.PlayerProgressResponse;
import com.sports.repository.UserRepository;
import com.sports.service.PerformanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    private final PerformanceService performanceService;
    private final UserRepository userRepository;

    public PerformanceController(PerformanceService performanceService, UserRepository userRepository) {
        this.performanceService = performanceService;
        this.userRepository = userRepository;
    }

    @GetMapping("/progress")
    public ResponseEntity<ApiResponse<List<PlayerProgressResponse>>> getAllProgress(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = resolveUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success("All sport progress", performanceService.getAllProgressForUser(userId)));
    }

    @GetMapping("/progress/{sportId}")
    public ResponseEntity<ApiResponse<PlayerProgressResponse>> getProgressBySport(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long sportId) {
        Long userId = resolveUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success("Sport progress", performanceService.getProgressBySport(userId, sportId)));
    }

    @GetMapping("/summary/{sportId}")
    public ResponseEntity<ApiResponse<List<PerformanceSummaryResponse>>> getPerformanceSummary(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long sportId) {
        Long userId = resolveUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success("Performance summary", performanceService.getPerformanceSummary(userId, sportId)));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<List<PerformanceSummaryResponse>>> getAllPerformance(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = resolveUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success("All performance summaries", performanceService.getAllPerformanceForUser(userId)));
    }

    private Long resolveUserId(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow()
                .getId();
    }
}
