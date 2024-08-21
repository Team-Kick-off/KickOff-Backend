package com.example.kickoffbackend.team.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {

    M("남자"),
    W("여자"),
    ALL("혼성");

    private final String text;
}
