package com.sports.service;

import com.sports.dto.response.SportResponse;
import com.sports.exception.ResourceNotFoundException;
import com.sports.model.Sport;
import com.sports.repository.SportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SportService {

    private final SportRepository sportRepository;

    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    public List<SportResponse> getAllSports() {
        return sportRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<SportResponse> getOlympicSports() {
        return sportRepository.findByOlympicSport(true)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SportResponse getSportById(Long id) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id: " + id));
        return mapToResponse(sport);
    }

    public Sport findById(Long id) {
        return sportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id: " + id));
    }

    private SportResponse mapToResponse(Sport sport) {
        return SportResponse.builder()
                .id(sport.getId())
                .name(sport.getName())
                .description(sport.getDescription())
                .category(sport.getCategory())
                .olympicSport(sport.isOlympicSport())
                .iconUrl(sport.getIconUrl())
                .matchesPerLevel(sport.getMatchesPerLevel())
                .winsRequiredToAdvance(sport.getWinsRequiredToAdvance())
                .scoreType(sport.getScoreType())
                .build();
    }
}
