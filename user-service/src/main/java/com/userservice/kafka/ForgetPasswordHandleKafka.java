package com.userservice.kafka;

import com.core.constants.FConstants;
import com.core.dto.request.MailSenderRequest;
import com.core.kafka.BaseKafkaHandler;
import com.core.utils.JavaMailSenderUtil;
import com.userservice.entities.ForgetPassword;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
@EnableKafka
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ForgetPasswordHandleKafka extends BaseKafkaHandler<ForgetPassword> {

    @KafkaListener(topics = FConstants.TOPIC_USER_FORGET_PW,
            groupId = FConstants.GROUP_ID_DEFAULT)
    public void listen(String rawMessage) {
        handleMessage(rawMessage, FConstants.TOPIC_USER_FORGET_PW, ForgetPassword.class);
    }

    @Override
    protected void processMessage(ForgetPassword message) {
        try {
            JavaMailSenderUtil.sendMailWithTemplate(MailSenderRequest.builder()
                    .sendTo(message.getEmail())
                    .subject("FaceClone - Quên mật khẩu")
                    .templateName("forget-password-send-mail")
                    .context(buildContext(message))
                    .build());
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage(), e);
        }
    }

    private Context buildContext(ForgetPassword forgetPassword) {
        Context context = new Context();
        context.setVariable("fullName", forgetPassword.getFullName());
        context.setVariable("verificationCode", forgetPassword.getVerificationCode());

        return context;
    }

}
