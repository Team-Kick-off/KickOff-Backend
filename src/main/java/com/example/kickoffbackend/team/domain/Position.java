package com.example.kickoffbackend.team.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Position {
    FORWARD("공격수"),
    MIDFIELDER("미드필더"),
    DEFENDER("수비수"),
    GOALKEEPER("골기퍼");

    private final String message;

}
