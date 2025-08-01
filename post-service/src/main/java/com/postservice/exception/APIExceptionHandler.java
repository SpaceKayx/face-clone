//package com.postservice.exception;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.security.NoSuchAlgorithmException;
//import java.sql.SQLException;
//
//@Slf4j
//@RestControllerAdvice
//public class APIExceptionHandler extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler(BaseException.class)
//    public ResponseEntity<DataResponse> handleBaseException(BaseException e) {
//        return buildResponseEntity(e.getErrorCode());
//    }
//
//    // ✅ Ghi đè method từ ResponseEntityExceptionHandler (không dùng @ExceptionHandler)
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex,
//            HttpHeaders headers,
//            HttpStatusCode status,
//            WebRequest request) {
//
//        String message = ex.getBindingResult().getFieldErrors().stream()
//                .map(error -> error.getDefaultMessage())
//                .reduce((msg1, msg2) -> msg1 + " | " + msg2)
//                .orElse("Dữ liệu không hợp lệ");
//
//        return ResponseEntity.badRequest().body(
//                DataResponse.builder()
//                        .code(400)
//                        .message(message)
//                        .build()
//        );
//    }
//
//    @ExceptionHandler(NoSuchAlgorithmException.class)
//    public ResponseEntity<DataResponse> handleNoSuchAlgorithmException(NoSuchAlgorithmException ex) {
//        return ResponseEntity.badRequest().body(
//                DataResponse.builder()
//                        .code(400)
//                        .message(ex.getMessage())
//                        .build()
//        );
//    }
//
//    @ExceptionHandler(UniqueException.class)
//    public ResponseEntity<DataResponse> handleUniqueException(UniqueException e) {
//        return buildResponseEntity(e.getErrorCode());
//    }
//
//    private ResponseEntity<DataResponse> buildResponseEntity(ErrorCode errorCode) {
//        log.error("Error: {}", errorCode);
//        DataResponse response = buildDataResponse(errorCode.getCode(), errorCode.getMessage());
//        log.error("DataResponse: {}", response);
//
//        return ResponseEntity.status(errorCode.getStatus()).body(response);
//    }
//
//    private DataResponse buildDataResponse(int code, String message) {
//        return DataResponse.builder()
//                .code(code)
//                .message(message)
//                .build();
//    }
//}
