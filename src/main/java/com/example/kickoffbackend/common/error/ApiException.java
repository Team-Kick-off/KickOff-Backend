package com.example.kickoffbackend.common.error;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class ApiException extends RuntimeException{
    private final ErrorCode errorCode;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
