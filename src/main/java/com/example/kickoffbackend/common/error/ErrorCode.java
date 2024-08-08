package com.example.kickoffbackend.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    // User 관련 에러 코드는 1000번대 사용
    EmailAlreadyRegisteredError(HttpStatus.BAD_REQUEST.value(), 1400, "이미 가입된 이메일입니다.");


    private int httpStatusCode;
    private int code;
    private String message;
}
