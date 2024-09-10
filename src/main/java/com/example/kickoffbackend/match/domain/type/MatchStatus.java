package com.example.kickoffbackend.match.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchStatus {
    RECRUITING("주최중"),
    RECRUITMENT_ENDS("주최 완료"),
    MATCHING("경기중"),
    MATCHING_ENDS("경기 종료");

    private final String massage;
}
