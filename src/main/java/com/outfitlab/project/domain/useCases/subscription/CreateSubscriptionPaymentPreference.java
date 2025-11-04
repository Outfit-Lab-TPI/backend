package com.outfitlab.project.domain.useCases.subscription;

import com.outfitlab.project.domain.exception.InvalidPlanException;
import com.outfitlab.project.domain.interfaces.repositories.PaymentGatewayRepository;
import com.outfitlab.project.domain.interfaces.repositories.SubscriptionPlanRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.PaymentPreferenceModel;
import com.outfitlab.project.domain.model.SubscriptionPlanModel;
import com.outfitlab.project.domain.model.UserModel;

/**
 * Caso de uso: Crear preferencia de pago para una suscripción.
 * Este Use Case pertenece al dominio y NO debe depender de SDKs externos.
 */
public class CreateSubscriptionPaymentPreference {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final UserRepository userRepository;
    private final PaymentGatewayRepository paymentGatewayRepository;

    public CreateSubscriptionPaymentPreference(
            SubscriptionPlanRepository subscriptionPlanRepository,
            UserRepository userRepository,
            PaymentGatewayRepository paymentGatewayRepository
    ) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.userRepository = userRepository;
        this.paymentGatewayRepository = paymentGatewayRepository;
    }

    /**
     * Ejecuta la creación de preferencia de pago.
     *
     * @param planCode Código del plan (ej: PREMIUM_MONTHLY)
     * @param userEmail Email del usuario
     * @return URL de inicio de pago (initPoint)
     * @throws InvalidPlanException si el plan no existe o no está activo
     */
    public String execute(String planCode, String userEmail) {
        // Validar que el plan existe
        SubscriptionPlanModel plan = subscriptionPlanRepository.findByPlanCode(planCode);
        if (plan == null) {
            throw new InvalidPlanException("Plan no encontrado: " + planCode);
        }

        // Validar que el plan está activo
        if (plan.getActive() == null || !plan.getActive()) {
            throw new InvalidPlanException("Plan no disponible: " + planCode);
        }

        // Validar que el usuario existe
        UserModel user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InvalidPlanException("Usuario no encontrado: " + userEmail));

        // Crear preferencia de pago usando el gateway (abstracción del SDK)
        PaymentPreferenceModel preference = paymentGatewayRepository.createPaymentPreference(
                plan.getPlanCode(),
                plan.getName(),
                plan.getPrice(),
                userEmail
        );

        return preference.getInitPoint();
    }
}