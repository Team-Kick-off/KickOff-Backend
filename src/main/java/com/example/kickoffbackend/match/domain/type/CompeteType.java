package com.example.kickoffbackend.match.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompeteType {
    HomeTeam("주최팀"),
    AwayTeam("상대팀");

    public final String massage;
}
