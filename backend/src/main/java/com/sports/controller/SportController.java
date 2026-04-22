package com.sports.controller;

import com.sports.dto.response.ApiResponse;
import com.sports.dto.response.SportResponse;
import com.sports.service.SportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sports")
public class SportController {

    private final SportService sportService;

    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SportResponse>>> getAllSports() {
        return ResponseEntity.ok(ApiResponse.success("Sports fetched successfully", sportService.getAllSports()));
    }

    @GetMapping("/olympic")
    public ResponseEntity<ApiResponse<List<SportResponse>>> getOlympicSports() {
        return ResponseEntity.ok(ApiResponse.success("Olympic sports fetched", sportService.getOlympicSports()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SportResponse>> getSportById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Sport fetched", sportService.getSportById(id)));
    }
}
