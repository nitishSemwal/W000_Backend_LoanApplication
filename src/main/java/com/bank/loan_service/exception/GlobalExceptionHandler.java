package com.bank.loan_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> catchInvalidMethodArguments(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        message.append(
                                error.getField())
                                .append(": ")
                                .append(error.getDefaultMessage())
                                .append(" "));

        log.info("Invalid Argument Exception Caught : message - {}", message);
        ErrorResponse errorResponse = new ErrorResponse("101", message.toString(), "API");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> catchBusinessException(String message) {
        log.info("Business Exception Caught : message - {}", message);
        ErrorResponse errorResponse = new ErrorResponse("102", message, "API");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
