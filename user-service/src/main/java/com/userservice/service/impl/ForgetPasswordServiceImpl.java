package com.userservice.service.impl;

import com.core.constants.FConstants;
import com.core.kafka.message.BaseMessage;
import com.core.kafka.producer.BaseProducerHandler;
import com.core.utils.CaptchaGenerator;
import com.userservice.dto.request.ForgetPasswordRequest;
import com.userservice.entities.ForgetPassword;
import com.userservice.entities.User;
import com.userservice.enums.LockedEnum;
import com.userservice.exception.BaseException;
import com.userservice.exception.ErrorCode;
import com.userservice.repository.ForgetPasswordRepository;
import com.userservice.service.abs.ForgetPasswordService;
import com.userservice.service.abs.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ForgetPasswordServiceImpl implements ForgetPasswordService {

    ForgetPasswordRepository repository;
    UserService userService;
    BaseProducerHandler kafka;

    @Override
    public void create(String email) {
        User user = userService.existsEmail(email);
        if(user.getLocked() == LockedEnum.LOCKED.getValue()){
            throw new BaseException(ErrorCode.ACCOUNT_LOCKED);
        }
        String captcha = CaptchaGenerator.generateCaptcha();

        ForgetPassword forgetPassword = ForgetPassword.builder()
                .userId(user.getId().toString())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .verificationCode(captcha)
                .build();

        kafka.send(BaseMessage.builder()
                .topic(FConstants.TOPIC_USER_FORGET_PW)
                .key(user.getId().toString())
                .value(forgetPassword)
                .build());

        repository.save(forgetPassword);
    }

    @Override
    public void validateCaptchaAndUpdatePassword(ForgetPasswordRequest request) throws NoSuchAlgorithmException {
        User user = userService.existsEmail(request.getEmail());

        ForgetPassword forgetPassword = this.getByCaptcha(request.getCaptcha(), user.getId().toString());
        forgetPassword.setUsed(true);
        repository.save(forgetPassword);

        userService.forgetPassword(user, request.getPassword());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<ForgetPassword> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ForgetPassword getByCaptcha(String captcha, String userId) {
        ForgetPassword response = repository.findFirstByVerificationCodeAndUserIdOrderByCreatedAtDesc(captcha, userId)
                .orElseThrow(() -> new BaseException(ErrorCode.CAPTCHA_ERROR));

        if (response.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BaseException(ErrorCode.CAPTCHA_EXPIRED);  // Ném exception nếu hết hạn
        }

        if (response.isUsed()) {
            throw new BaseException(ErrorCode.CAPTCHA_ALREADY_USED);  // Ném exception nếu mã đã được sử dụng
        }

        return response;
    }


    @Override
    public List<ForgetPassword> getAll() {
        return repository.findAll();
    }

}