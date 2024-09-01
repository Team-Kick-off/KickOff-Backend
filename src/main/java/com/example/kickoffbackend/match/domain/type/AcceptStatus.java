package com.example.kickoffbackend.match.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AcceptStatus {
    ClOSE("수락요청"),
    OPEN("수락완료");

    private final String massage;
}
