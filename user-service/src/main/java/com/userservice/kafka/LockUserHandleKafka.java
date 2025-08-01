package com.userservice.kafka;

import com.core.constants.FConstants;
import com.core.kafka.message.BaseMessage;
import com.userservice.entities.ForgetPassword;
import com.userservice.service.abs.UserService;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@EnableKafka
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LockUserHandleKafka {

    SpringTemplateEngine templateEngine;
    JavaMailSender mailSender;
    UserService userService;

    @KafkaListener(topics = FConstants.TOPIC_USER_LOCKED,
            groupId = FConstants.GROUP_ID_DEFAULT)
    public void listen(String rawMessage) {
        log.info("Start handle send mail locked user");
        if (rawMessage == null) return;
        try {
            ForgetPassword forgetPassword = new BaseMessage().getValue(ForgetPassword.class, rawMessage);
            if (forgetPassword == null) return;
            userService.lockAccount(forgetPassword.getUserId());
            String contentHtml = templateEngine.process("user-locked-send-mail", new Context());
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(forgetPassword.getEmail());
            helper.setSubject("FaceClone - Tài khoản đã bị khóa");
            helper.setText(contentHtml, true);

            mailSender.send(mimeMessage);
            log.info("End handle send mail");
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage(), e);
        }
    }

}
