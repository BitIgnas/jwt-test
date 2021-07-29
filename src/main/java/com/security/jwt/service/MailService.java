package com.security.jwt.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.URI;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    public void sendMail(String to, String token) {
        String link = "http://localhost:8080/api/auth/user/verify/" + token;

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("IgnasTestingas@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject("Please activate your account");
            messageHelper.setText(mailContentBuilder.build(link), true);

            javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            log.error(e.getMessage());
        }

    }
}
