package com.core.utils;

import com.core.dto.request.MailSenderRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JavaMailSenderUtil {

    static SpringTemplateEngine templateEngine;
    static JavaMailSender mailSender;

    public static void sendMailWithTemplate(MailSenderRequest request) throws MessagingException {
        try {
            log.info("Starting mail success to: {}", request.getSendTo());
            String contentWithHtml = templateEngine.process(request.getTemplateName(), request.getContext());
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(request.getSendTo());
            helper.setSubject(request.getSubject());
            helper.setText(contentWithHtml, true);

            mailSender.send(mimeMessage);
            log.info("Send mail success");
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage(), e);
        }
    }

}
