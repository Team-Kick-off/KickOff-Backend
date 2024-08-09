package com.example.kickoffbackend.common;

import com.example.kickoffbackend.common.error.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomApi<T> {
    private int code;
    private String message;
    private T data;

    @Builder
    private CustomApi(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CustomApi<T> OK(T data){
        return (CustomApi<T>) CustomApi.builder()
                .code(200)
                .message("성공")
                .data(data)
                .build();
    }

    public static <T> CustomApi<T> OK(T data, String message){
        return (CustomApi<T>) CustomApi.builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> CustomApi<T> OK(String message){
        return (CustomApi<T>) CustomApi.builder()
                .code(200)
                .message(message)
                .build();
    }

    public static CustomApi ERROR(Exception e) {
        return CustomApi.builder()
                .code(500)
                .message(e.getLocalizedMessage())
                .build();
    }

    public static CustomApi ERROR(ErrorCode errorCode) {
        return CustomApi.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static CustomApi ERROR(int code, String message) {
        return CustomApi.builder()
                .code(code)
                .message(message)
                .build();
    }
}
