package com.outfitlab.project.domain.interfaces.gateways;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;

public interface MercadoPagoPaymentGateway {
    Payment getPaymentDetails(Long paymentId) throws MPException, MPApiException;
}