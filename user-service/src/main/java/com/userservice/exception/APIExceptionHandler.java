package com.userservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity handleBaseException(BaseException e) {
        return buildResponseEntity(e.getErrorCode());
    }

    // Ghi đè method từ ResponseEntityExceptionHandler (không dùng @ExceptionHandler)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + " | " + msg2)
                .orElse("Dữ liệu không hợp lệ");

        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity handleNoSuchAlgorithmException(NoSuchAlgorithmException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class, SQLException.class})
    public ResponseEntity handleDataIntegrityViolationException(Exception e) {
        String message = e.getMessage();
        log.error("Data Integrity Violation: {}", message);

        if (e instanceof DataIntegrityViolationException) {
            if (message.contains("(email)")) {
                return buildResponseEntity(ErrorCode.EMAIL_UNIQUE);
            } else if (message.contains("(username)")) {
                return buildResponseEntity(ErrorCode.USERNAME_UNIQUE);
            } else if (message.contains("(phone_number)")) {
                return buildResponseEntity(ErrorCode.PHONE_UNIQUE);
            }
        }

        return buildResponseEntity(ErrorCode.UNKNOWN_UNIQUE);
    }

    @ExceptionHandler(UniqueException.class)
    public ResponseEntity handleUniqueException(UniqueException e) {
        return buildResponseEntity(e.getErrorCode());
    }

    private ResponseEntity buildResponseEntity(ErrorCode errorCode) {
        log.error("Error: {}", errorCode);
        return ResponseEntity
                .status(errorCode.getCode())
                .body(errorCode.getMessage());
    }

}
