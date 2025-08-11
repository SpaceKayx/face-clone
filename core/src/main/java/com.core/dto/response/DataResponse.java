package com.core.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataResponse {
    HttpStatus code = HttpStatus.OK;
    String message = "Thao tác thành công!";
    Object data;
    LocalDateTime responseTime = LocalDateTime.now();

    public DataResponse(Object data) {
        this.code = HttpStatus.OK;
        this.data = data;
        this.message = "Thao tác thành công!";
        this.responseTime = LocalDateTime.now();
    }

    public DataResponse(String message) {
        this.code = HttpStatus.OK;
        this.data = null;
        this.message = message;
        this.responseTime = LocalDateTime.now();
    }

}
