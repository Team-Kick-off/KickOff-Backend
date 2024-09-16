package com.example.kickoffbackend.match.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompeteType {
    HOME_TEAM("주최팀"),
    HOME_TEAM_HOST("주최한 운영진"),
    AWAY_TEAM("상대팀");

    public final String massage;
}
