package com.userservice.exception;

import com.core.dto.response.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<DataResponse> handleBaseException(BaseException ex) {
        log.error("[BaseException] {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getErrorCode().getStatus(), ex.getErrorCode().getMessage());
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<DataResponse> handleNoSuchAlgorithmException(NoSuchAlgorithmException ex) {
        log.error("[NoSuchAlgorithmException] {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class, SQLException.class})
    public ResponseEntity<DataResponse> handleDataIntegrityViolationException(Exception ex) {
        log.error("[DataIntegrityViolationException] {}", ex.getMessage(), ex);
        String message = ex.getMessage();

        if (ex instanceof DataIntegrityViolationException) {
            if (message.contains("(email)")) {
                return buildErrorResponse(ErrorCode.EMAIL_UNIQUE);
            } else if (message.contains("(username)")) {
                return buildErrorResponse(ErrorCode.USERNAME_UNIQUE);
            } else if (message.contains("(phone_number)")) {
                return buildErrorResponse(ErrorCode.PHONE_UNIQUE);
            }
        }

        return buildErrorResponse(ErrorCode.UNKNOWN_UNIQUE);
    }

    @ExceptionHandler(UniqueException.class)
    public ResponseEntity<DataResponse> handleUniqueException(UniqueException ex) {
        log.error("[UniqueException] {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getErrorCode());
    }

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

        log.warn("[ValidationException] {}", message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(DataResponse.builder()
                        .code(HttpStatus.BAD_REQUEST)
                        .message(message)
                        .build());
    }

    private ResponseEntity<DataResponse> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(DataResponse.builder()
                        .code(status)
                        .message(message)
                        .build());
    }

    private ResponseEntity<DataResponse> buildErrorResponse(ErrorCode errorCode) {
        return buildErrorResponse(errorCode.getStatus(), errorCode.getMessage());
    }
}
