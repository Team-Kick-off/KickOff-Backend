package com.example.kickoffbackend.user.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Sex {
    M("남자"),
    W("여자");

    private String message;
}
