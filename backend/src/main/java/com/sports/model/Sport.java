package com.sports.model;

import com.sports.model.enums.SportCategory;
import com.sports.model.enums.SportScoreType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SportCategory category;

    @Column(name = "is_olympic_sport", nullable = false)
    private boolean olympicSport;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "matches_per_level", nullable = false)
    private int matchesPerLevel = 5;

    @Column(name = "wins_required_to_advance", nullable = false)
    private int winsRequiredToAdvance = 3;

    @Enumerated(EnumType.STRING)
    @Column(name = "score_type")
    private SportScoreType scoreType;
}
