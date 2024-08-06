package com.example.kickoffbackend.exceptionHandler;

import com.example.kickoffbackend.common.CustomApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(value = Integer.MAX_VALUE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<CustomApi> exception(Exception e) {
        log.error("", e);

        return ResponseEntity
                .status(500)
                .body(CustomApi.ERROR(e));
    }
}
