package dna.safe_guard.service;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String sender;
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String subject, String body, String... receiver) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setFrom(sender);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);

        log.info("메일 전송: 제목={}, 수신자={}", subject, String.join(", ", receiver));
    }

}