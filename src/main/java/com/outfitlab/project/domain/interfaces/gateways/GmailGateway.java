package com.outfitlab.project.domain.interfaces.gateways;

public interface GmailGateway {
    void sendEmail(String toEmail, String subject, String body);
}
