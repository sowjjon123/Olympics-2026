package com.sports.dto.response;

import com.sports.model.enums.GameLevel;
import com.sports.model.enums.MatchResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponse {
    private Long id;
    private Long sportId;
    private String sportName;
    private GameLevel level;
    private Integer playerScore;
    private Integer opponentScore;
    private MatchResult result;
    private int matchNumberInLevel;
    private LocalDateTime playedAt;
    private String matchNotes;
    private Map<String, Object> scoreDetails;
}
