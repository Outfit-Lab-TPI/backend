package com.outfitlab.project.infrastructure.gateways;

import com.outfitlab.project.domain.interfaces.gateways.GmailGateway;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Slf4j
public class GmailGatewayImpl implements GmailGateway {

    private final JavaMailSender mailSender;
    private final String senderEmail;

    public GmailGatewayImpl(JavaMailSender mailSender, String senderEmail) {
        this.mailSender = mailSender;
        this.senderEmail = senderEmail;
    }

    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);

            log.info("Enviando email SMTP to={} subject={}", toEmail, subject);
            mailSender.send(message);
            log.info("Correo de suscripción enviado a {}", toEmail);
        } catch (jakarta.mail.MessagingException e) {
            log.error("ERROR al enviar correo a {}: {}", toEmail, e.getMessage(), e);
        }
    }
}
