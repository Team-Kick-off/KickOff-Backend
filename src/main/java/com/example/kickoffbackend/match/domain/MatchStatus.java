package com.example.kickoffbackend.match.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchStatus {
    RECRUITING("모집중"),
    RECRUITMENT_ENDS("모집 종료"),
    MATCHING("경기중"),
    MATCHING_ENDS("경기 종료");

    private final String text;
}
