package com.sports.model;

import com.sports.model.enums.GameLevel;
import com.sports.model.enums.MatchResult;
import com.sports.util.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameLevel level;

    @Column(name = "player_score")
    private Integer playerScore;

    @Column(name = "opponent_score")
    private Integer opponentScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchResult result = MatchResult.PENDING;

    @Column(name = "match_number_in_level")
    private int matchNumberInLevel;

    @Column(name = "played_at")
    private LocalDateTime playedAt;

    @Column(name = "match_notes", length = 500)
    private String matchNotes;

    @Column(name = "score_details", columnDefinition = "TEXT")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> scoreDetails;

    @PrePersist
    protected void onCreate() {
        playedAt = LocalDateTime.now();
    }
}
