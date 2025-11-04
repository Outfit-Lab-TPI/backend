package com.outfitlab.project.domain.useCases.subscription;

import com.outfitlab.project.domain.exception.InvalidPlanException;
import com.outfitlab.project.domain.exception.PaymentFailedException;
import com.outfitlab.project.domain.interfaces.repositories.PaymentGatewayRepository;
import com.outfitlab.project.domain.interfaces.repositories.SubscriptionPlanRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import com.outfitlab.project.domain.model.PaymentStatusModel;
import com.outfitlab.project.domain.model.SubscriptionPlanModel;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.model.UserSubscriptionModel;
import com.outfitlab.project.domain.model.enums.SubscriptionStatus;

import java.time.LocalDateTime;

/**
 * Caso de uso: Procesar el pago de una suscripción tras confirmación del gateway.
 * Se ejecuta cuando el webhook de MercadoPago notifica un pago aprobado.
 */
public class ProcessSubscriptionPayment {

    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PaymentGatewayRepository paymentGatewayRepository;

    public ProcessSubscriptionPayment(
            UserRepository userRepository,
            UserSubscriptionRepository userSubscriptionRepository,
            SubscriptionPlanRepository subscriptionPlanRepository,
            PaymentGatewayRepository paymentGatewayRepository
    ) {
        this.userRepository = userRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.paymentGatewayRepository = paymentGatewayRepository;
    }

    /**
     * Procesa el pago de una suscripción.
     *
     * @param paymentId ID del pago en el gateway (MercadoPago)
     * @throws PaymentFailedException si el pago no está aprobado
     * @throws InvalidPlanException si no se puede determinar el plan
     */
    public void execute(String paymentId) {
        // Obtener información del pago desde el gateway
        PaymentStatusModel paymentStatus = paymentGatewayRepository.getPaymentStatus(paymentId);

        // Validar que el pago fue aprobado
        if (!paymentStatus.isApproved()) {
            throw new PaymentFailedException(
                    "El pago no está aprobado. Estado: " + paymentStatus.getStatus() +
                    ", Detalle: " + paymentStatus.getStatusDetail()
            );
        }

        // Obtener email del usuario desde el pago
        String userEmail = paymentStatus.getPayerEmail();
        if (userEmail == null || userEmail.isEmpty()) {
            throw new PaymentFailedException("No se pudo obtener el email del usuario del pago");
        }

        // Buscar usuario
        UserModel user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new PaymentFailedException("Usuario no encontrado: " + userEmail));

        // Extraer el código del plan desde externalReference (formato: "PREMIUM_MONTHLY_user@example.com")
        String externalReference = paymentStatus.getExternalReference();
        String planCode = extractPlanCodeFromReference(externalReference);
        
        if (planCode == null) {
            throw new InvalidPlanException("No se pudo determinar el plan desde la referencia: " + externalReference);
        }

        // Buscar el plan
        SubscriptionPlanModel plan = subscriptionPlanRepository.findByPlanCode(planCode);
        if (plan == null) {
            throw new InvalidPlanException("Plan no encontrado: " + planCode);
        }

        // Crear registro de suscripción
        UserSubscriptionModel userSubscription = new UserSubscriptionModel();
        userSubscription.setUser(user);
        userSubscription.setPlan(plan);
        userSubscription.setMercadoPagoPaymentId(paymentId);
        userSubscription.setStatus(SubscriptionStatus.PREMIUM_ACTIVE);
        userSubscription.setStartDate(LocalDateTime.now());
        userSubscription.setEndDate(LocalDateTime.now().plusDays(plan.getDurationDays()));
        userSubscription.setAutoRenew(false);

        // Guardar la suscripción
        userSubscriptionRepository.save(userSubscription);

        // Actualizar estado del usuario
        user.setSubscriptionStatus(SubscriptionStatus.PREMIUM_ACTIVE);
        user.setSubscriptionExpiresAt(userSubscription.getEndDate());
        userRepository.save(user);
    }

    /**
     * Extrae el código del plan desde la referencia externa.
     * Formato esperado: "PREMIUM_MONTHLY_user@example.com"
     */
    private String extractPlanCodeFromReference(String externalReference) {
        if (externalReference == null || externalReference.isEmpty()) {
            return null;
        }
        
        // Si contiene underscore, tomar la primera parte
        int underscoreIndex = externalReference.indexOf('_');
        if (underscoreIndex > 0) {
            // Buscar el segundo underscore para obtener "PREMIUM_MONTHLY"
            int secondUnderscoreIndex = externalReference.indexOf('_', underscoreIndex + 1);
            if (secondUnderscoreIndex > 0) {
                return externalReference.substring(0, secondUnderscoreIndex);
            }
        }
        
        return externalReference;
    }
}