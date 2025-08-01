package com.userservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    REGISTER_ERROR(HttpStatus.BAD_REQUEST.value(), "Đăng ký không thành công", HttpStatus.BAD_REQUEST),
    LOGIN_ERROR(HttpStatus.BAD_REQUEST.value(), "Vui lòng kiểm tra lại username và password", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOTFOUND(HttpStatus.NOT_FOUND.value(), "Không tìm thấy tài khoản", HttpStatus.NOT_FOUND),
    ACCOUNT_LOCKED(HttpStatus.LOCKED.value(), "Tài khoản đã bị khóa", HttpStatus.LOCKED),
    EMAIL_NOTFOUND(HttpStatus.NOT_FOUND.value(), "Không tìm thấy email", HttpStatus.NOT_FOUND),
    PASSWORD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Có lỗi trong quá trình cập nhật mật khẩu", HttpStatus.INTERNAL_SERVER_ERROR),
    PASSWORD_INVALID(HttpStatus.BAD_REQUEST.value(), "Mật khẩu không đúng", HttpStatus.BAD_REQUEST),

    CAPTCHA_ERROR(HttpStatus.BAD_REQUEST.value(), "Captcha không hợp lệ", HttpStatus.BAD_REQUEST),
    CAPTCHA_EXPIRED(HttpStatus.BAD_REQUEST.value(), "Mã captcha đã hết hạn", HttpStatus.BAD_REQUEST),
    CAPTCHA_ALREADY_USED(HttpStatus.BAD_REQUEST.value(), "Mã captcha đã được sử dụng", HttpStatus.BAD_REQUEST),

    USERNAME_UNIQUE(HttpStatus.CONFLICT.value(), "Tên người dùng đã tồn tại", HttpStatus.CONFLICT),
    PHONE_UNIQUE(HttpStatus.CONFLICT.value(), "Số điện thoại đã được sử dụng", HttpStatus.CONFLICT),
    EMAIL_UNIQUE(HttpStatus.CONFLICT.value(), "Email đã được đăng ký", HttpStatus.CONFLICT),
    UNKNOWN_UNIQUE(HttpStatus.CONFLICT.value(), "Thông tin đã tồn tại", HttpStatus.CONFLICT)
    ;

    private final int code;
    private final String message;
    private final HttpStatus status;
}

