package com.example.kickoffbackend.exceptionHandler;

import com.example.kickoffbackend.common.CustomApi;
import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.common.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(value = Integer.MIN_VALUE)
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<CustomApi> exception(ApiException e){
        log.error("{}", e);

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(CustomApi.ERROR(errorCode));
    }
}
