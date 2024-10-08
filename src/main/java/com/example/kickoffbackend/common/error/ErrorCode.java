package com.example.kickoffbackend.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    // User 관련 에러 코드는 1000번대 사용
    EMAIL_ALREADY_REGISTERED_ERROR(HttpStatus.BAD_REQUEST.value(), 1400, "이미 가입된 이메일입니다."),
    LOGIN_BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST.value(), 1401, "로그인 형식이 잘못됐습니다."),
    FAILED_LOGIN_ERROR(HttpStatus.BAD_REQUEST.value(), 1402, "아이디 또는 비밀번호가 잘못됐습니다."),
    USER_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST.value(), 1403, "유저를 찾을 수 없습니다."),
    EMAIL_BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST.value(), 1404, "이메일 인증 실패"),


    // Team 관련 에러 코드는 2000번대 사용
    TEAM_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 2401 , "팀이 존재하지 않습니다."),
    ALREADY_JOINED_TEAM_ERROR(HttpStatus.BAD_REQUEST.value(),2402 , "팀에 가입된 유저입니다."),

    // Match 관련 에러 코드는 3000번대 사용
    MATCH_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 3400, "경기가 존재하지 않습니다."),
    FIELD_ALREADY_RESERVATION_ERROR(HttpStatus.BAD_REQUEST.value(), 3401, "이미 예약중인 경기장입니다.");

    private int httpStatusCode;
    private int code;
    private String message;
}
