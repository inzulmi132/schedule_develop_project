package com.sparta.scheduledevelop.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException e) {
        CustomErrorCode errorCode = e.getCustomErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorCode.getMessage());
    }
}
