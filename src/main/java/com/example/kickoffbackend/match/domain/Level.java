package com.example.kickoffbackend.match.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {
    ROOKIE("루키"),       // 아직 풋살 경험이 적어 실력이 베일에 쌓여있어요
    STARTER("스타터"),     // 이제 풋살을 시작했어요
    BEGINNER("비기너"),    // 패스 드리블 컨트롤을 갈고닦고 있어요
    AMATEUR("아마추어"),    // 기본기를 갖추고 실전 경험 쌓으며 성장하고 있어요
    SEMIPRO("세미프로"),    // 일반인 에이스, 안정적인 공격과 수비
    PRO("프로");           // 고등학교 이상 선수 출신의 실력이에요

    private final String message;
}

