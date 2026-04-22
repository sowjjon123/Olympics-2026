package com.sports.dto.response;

import com.sports.model.enums.SportCategory;
import com.sports.model.enums.SportScoreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportResponse {
    private Long id;
    private String name;
    private String description;
    private SportCategory category;
    private boolean olympicSport;
    private String iconUrl;
    private int matchesPerLevel;
    private int winsRequiredToAdvance;
    private SportScoreType scoreType;
}
