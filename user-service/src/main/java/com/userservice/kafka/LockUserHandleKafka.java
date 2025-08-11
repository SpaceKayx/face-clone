package com.userservice.kafka;

import com.core.constants.FConstants;
import com.core.dto.request.MailSenderRequest;
import com.core.kafka.BaseKafkaHandler;
import com.core.kafka.message.BaseMessage;
import com.core.utils.JavaMailSenderUtil;
import com.userservice.entities.ForgetPassword;
import com.userservice.service.abs.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LockUserHandleKafka extends BaseKafkaHandler<ForgetPassword> {

    UserService userService;

    @KafkaListener(topics = FConstants.TOPIC_USER_LOCKED,
            groupId = FConstants.GROUP_ID_DEFAULT)
    public void listen(String rawMessage) {
        handleMessage(rawMessage, FConstants.TOPIC_USER_LOCKED, ForgetPassword.class);
    }

    @Override
    protected void processMessage(ForgetPassword message) {
        try {
            userService.lockAccount(message.getUserId());

            JavaMailSenderUtil.sendMailWithTemplate(MailSenderRequest.builder()
                    .sendTo(message.getEmail())
                    .subject("FaceClone - Tài khoản đã bị khóa")
                    .templateName("user-locked-send-mail")
                    .build());
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage(), e);
        }
    }
}
