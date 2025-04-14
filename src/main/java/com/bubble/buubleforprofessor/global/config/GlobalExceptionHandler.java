package com.bubble.buubleforprofessor.global.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();// 에러코드 가져옴
        String timeStamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()); // 현재 시간 생성
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMsg(), errorCode.getStatus().value(), timeStamp);
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());

        //todo illlecpxtipn 추가

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedRollbackException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();

        String errorMessage = fieldError.getField() + ": " + fieldError.getDefaultMessage();
        String timeStamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()); // 현재 시간 생성
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value(), timeStamp);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
