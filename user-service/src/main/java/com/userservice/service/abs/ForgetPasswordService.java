package com.userservice.service.abs;

import com.userservice.dto.request.ForgetPasswordRequest;
import com.userservice.entities.ForgetPassword;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public interface ForgetPasswordService {
    void create(String email);
    void validateCaptchaAndUpdatePassword(ForgetPasswordRequest request) throws NoSuchAlgorithmException;
    void delete(Long id);
    Optional<ForgetPassword> getById(Long id);
    ForgetPassword getByCaptcha(String captcha, String userId);
    List<ForgetPassword> getAll();
}
