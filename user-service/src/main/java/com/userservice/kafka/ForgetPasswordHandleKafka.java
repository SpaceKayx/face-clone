package com.userservice.kafka;

import com.core.constants.FConstants;
import com.core.kafka.message.BaseMessage;
import com.userservice.entities.ForgetPassword;
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
public class ForgetPasswordHandleKafka {

    SpringTemplateEngine templateEngine;
    JavaMailSender mailSender;

    @KafkaListener(topics = FConstants.TOPIC_USER_FORGET_PW,
            groupId = FConstants.GROUP_ID_DEFAULT)
    public void listen(String rawMessage) {
        log.info("Start handle send mail");
        if (rawMessage == null) return;
        try {

            ForgetPassword forgetPassword = new BaseMessage().getValue(ForgetPassword.class, rawMessage);

            Context context = new Context();
            context.setVariable("fullName", forgetPassword.getFullName());
            context.setVariable("verificationCode", forgetPassword.getVerificationCode());

            String contentHtml = templateEngine.process("forget-password-send-mail", context);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(forgetPassword.getEmail());
            helper.setSubject("FaceClone - Quên mật khẩu");
            helper.setText(contentHtml, true);

            mailSender.send(mimeMessage);
            log.info("End handle send mail");
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage(), e);
        }
    }

}
