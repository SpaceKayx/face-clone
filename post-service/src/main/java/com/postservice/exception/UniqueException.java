package com.postservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataIntegrityViolationException;

@Getter
@Setter
public class UniqueException extends DataIntegrityViolationException {

    ErrorCode errorCode;

    public UniqueException(String msg) {
        super(msg);
    }

    public UniqueException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UniqueException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public UniqueException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

}
