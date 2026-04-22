package com.sports.model.enums;

public enum SportScoreType {
    POINTS,               // Generic points (basketball, volleyball, fencing, rugby, kho-kho, carrom, chess)
    GOALS,                // Goals scored (football, hockey)
    SETS,                 // Set-based (badminton, table tennis, tennis)
    CRICKET,              // Runs / wickets / overs
    KABADDI,              // Raid points + tackle points + bonus points
    TIME_LOWER_WINS,      // Fastest time wins (swimming, running, cycling, rowing, marathon)
    DISTANCE_HIGHER_WINS, // Farthest distance wins (long jump, javelin)
    COMBAT,               // Points + optional KO / Ippon (boxing, wrestling, judo, taekwondo, fencing)
    ACCURACY,             // Target score (shooting, archery)
    ARTISTIC_SCORE,       // Judges score (gymnastics, diving)
    WEIGHTLIFTING         // Snatch + Clean & Jerk combined kg
}
