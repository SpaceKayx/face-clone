package com.userservice.controller;

import com.core.dto.response.DataResponse;
import com.userservice.dto.request.ForgetPasswordRequest;
import com.userservice.service.abs.ForgetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/forget-password")
@RequiredArgsConstructor
public class ForgetPasswordController {

    private final ForgetPasswordService forgetPasswordService;

    @GetMapping("/check-email")
    public DataResponse checkEmailAndSendCaptcha(@RequestParam String email) {
        forgetPasswordService.create(email);

        return new DataResponse("Mã xác thực đã được gửi qua email!");
    }

    @PutMapping
    public DataResponse validateCaptchaAndUpdatePassword(@RequestBody ForgetPasswordRequest request) throws NoSuchAlgorithmException {
        forgetPasswordService.validateCaptchaAndUpdatePassword(request);

        return new DataResponse("Đổi mật khẩu người dùng thành công!");
    }

}
