package com.example.kickoffbackend.team.domain;

import lombok.AllArgsConstructor;



@AllArgsConstructor
public enum Role {
    ADMIN("관리자"),
    STAFF("부관리자"),
    MEMBER("팀원");

    private String message;

}
