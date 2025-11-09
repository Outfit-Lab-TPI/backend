package com.outfitlab.project.infrastructure.gateways;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.outfitlab.project.domain.interfaces.gateways.MercadoPagoPaymentGateway;

public class MercadoPagoPaymentGatewayImpl implements MercadoPagoPaymentGateway {

    @Override
    public Payment getPaymentDetails(Long paymentId) throws MPException, MPApiException {
        PaymentClient client = new PaymentClient();
        Payment payment = client.get(paymentId);

        return payment;
    }
}