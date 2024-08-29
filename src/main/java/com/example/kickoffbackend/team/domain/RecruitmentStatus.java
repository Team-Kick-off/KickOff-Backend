package com.example.kickoffbackend.team.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RecruitmentStatus {

    OPEN("모집중"),
    CLOSED("모집마감");

    public final String text;
}
