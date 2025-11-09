package com.outfitlab.project.domain.useCases.mercadopago;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;

public class ProcessPaymentNotification {

    private final UserRepository userRepository;

    public ProcessPaymentNotification(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long paymentId) throws MPException, MPApiException {
        PaymentClient client = new PaymentClient();
        Payment payment = client.get(paymentId);

        String status = payment.getStatus();
        String externalReference = payment.getExternalReference();

        System.out.printf("Procesando Webhook de PAGO. ID Pago MP: %s, ID Interno: %s, Status: %s%n",
                paymentId, externalReference, status);

        if ("approved".equals(status)) {
            System.out.println("Pago APPROVED. Activando Premium (Demo) para: " + externalReference);


        } else if ("rejected".equals(status) || "cancelled".equals(status)) {
            System.out.println("Pago REJECTED/CANCELLED para: " + externalReference);
        }
    }
}