package com.sports.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class MatchResultRequest {

    @NotNull(message = "Sport ID is required")
    private Long sportId;

    /** Used for backward-compatible simple scoring (optional when scoreDetails is provided). */
    @Min(value = 0, message = "Score cannot be negative")
    private Integer playerScore;

    @Min(value = 0, message = "Score cannot be negative")
    private Integer opponentScore;

    /** Sport-specific score breakdown. When present, takes priority over playerScore/opponentScore. */
    private Map<String, Object> scoreDetails;

    private String matchNotes;
}
