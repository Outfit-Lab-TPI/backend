package com.outfitlab.project.domain.useCases.gmail;

import com.outfitlab.project.domain.interfaces.gateways.GmailGateway;

public class SubscribeUser {

    private final GmailGateway gmailGateway;

    public SubscribeUser(GmailGateway gmailGateway) {
        this.gmailGateway = gmailGateway;
    }

    public void execute(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido.");
        }

        sendWelcomeEmail(email);
    }

    private void sendWelcomeEmail(String toEmail) {
        String subject = "¡Bienvenido a OutfitLab! 🥳";
        String body = """
            <html>
                <body>
                    <div style="font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd;">
                        <h2 style="color: #333;">¡Gracias por suscribirte a OutfitLab!</h2>
                        <p>Estamos muy emocionados de tenerte con nosotros. Serás el primero en recibir todas las novedades sobre nuestro probador virtual y las últimas tendencias.</p>
                        <p>¡Mantente atento a tu bandeja de entrada!</p>
                        <p style="margin-top: 30px;">El equipo de OutfitLab</p>
                    </div>
                </body>
            </html>
            """;

        gmailGateway.sendEmail(toEmail, subject, body);
    }
}