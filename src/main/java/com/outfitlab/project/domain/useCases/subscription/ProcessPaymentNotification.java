package com.outfitlab.project.domain.useCases.subscription;


import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.interfaces.gateways.MercadoPagoPaymentGateway;

public class ProcessPaymentNotification {

    private final UserRepository userRepository;
    private final MercadoPagoPaymentGateway paymentGateway;

    public ProcessPaymentNotification(UserRepository userRepository, MercadoPagoPaymentGateway paymentGateway) {
        this.userRepository = userRepository;
        this.paymentGateway = paymentGateway;
    }
    public void execute(Long paymentId) throws MPException, MPApiException {
        Payment payment = paymentGateway.getPaymentDetails(paymentId);

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