package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.gateways.GmailGateway;
import com.outfitlab.project.domain.useCases.gmail.SubscribeUser;
import com.outfitlab.project.infrastructure.gateways.GmailGatewayImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class GmailConfig {

    @Bean
    public GmailGateway gmailGateway(JavaMailSender mailSender, @Value("${spring.mail.username}") String senderEmail) {
        return new GmailGatewayImpl(mailSender, senderEmail);
    }

    @Bean
    public SubscribeUser subscribeUser(GmailGateway gmailGateway) {
        return new SubscribeUser(gmailGateway);
    }

}
