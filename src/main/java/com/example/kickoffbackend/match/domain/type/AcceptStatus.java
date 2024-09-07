package com.example.kickoffbackend.match.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AcceptStatus {
    CALL("수락요청"),
    ACCEPT("수락완료");

    private final String massage;
}
