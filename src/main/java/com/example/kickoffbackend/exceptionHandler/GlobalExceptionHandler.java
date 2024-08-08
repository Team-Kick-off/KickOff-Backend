package com.example.kickoffbackend.exceptionHandler;

import com.example.kickoffbackend.common.CustomApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(value = Integer.MAX_VALUE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<CustomApi> bindException(BindException e){
        log.error("{}", e.getAllErrors());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CustomApi.ERROR(400, e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<CustomApi> exception(Exception e) {
        log.error("{}", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CustomApi.ERROR(e));
    }
}

