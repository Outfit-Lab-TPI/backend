package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.PaymentPreferenceModel;
import com.outfitlab.project.domain.model.PaymentStatusModel;

import java.math.BigDecimal;

/**
 * Puerto (interfaz) para el gateway de pagos.
 * Define el contrato que debe implementar cualquier proveedor de pagos (MercadoPago, Stripe, etc.)
 * Esta interfaz pertenece al dominio y NO debe tener dependencias de infraestructura.
 */
public interface PaymentGatewayRepository {

    /**
     * Crea una preferencia de pago para una suscripción.
     *
     * @param planCode Código del plan (ej: PREMIUM_MONTHLY)
     * @param planName Nombre descriptivo del plan
     * @param price Precio del plan
     * @param userEmail Email del usuario que realizará el pago
     * @return PaymentPreferenceModel con los datos para redirigir al usuario
     */
    PaymentPreferenceModel createPaymentPreference(
            String planCode,
            String planName,
            BigDecimal price,
            String userEmail
    );

    /**
     * Obtiene el estado de un pago específico.
     *
     * @param paymentId ID del pago en el gateway
     * @return PaymentStatusModel con los datos del pago
     */
    PaymentStatusModel getPaymentStatus(String paymentId);
}
