package com.example.kickoffbackend.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_ACCOUNT_ERROR(400, 1400, "유효하지 않습니다.");

    private int httpStatusCode;
    private int code;
    private String message;
}
