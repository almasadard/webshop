package com.fairgoods.webshop.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(code = BAD_REQUEST)
@RequiredArgsConstructor
@Getter
public class BadRequestException extends RuntimeException {

    private final String errorMessage;

    @ExceptionHandler
    public static ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        String errorMessage = ex.getErrorMessage();
        return ResponseEntity.status(BAD_REQUEST).body(errorMessage);
    }
}