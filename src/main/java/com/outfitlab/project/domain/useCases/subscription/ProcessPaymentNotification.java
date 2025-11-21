package com.outfitlab.project.domain.useCases.subscription;


import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.SubscriptionRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.interfaces.gateways.MercadoPagoPaymentGateway;
import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import com.outfitlab.project.domain.model.SubscriptionModel;
import com.outfitlab.project.domain.model.UserSubscriptionModel;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessPaymentNotification {

    private final UserRepository userRepository;
    private final MercadoPagoPaymentGateway paymentGateway;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ProcessPaymentNotification(UserRepository userRepository, 
                                     MercadoPagoPaymentGateway paymentGateway,
                                     UserSubscriptionRepository userSubscriptionRepository,
                                     SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.paymentGateway = paymentGateway;
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.subscriptionRepository = subscriptionRepository;
    }
    
    public void execute(Long paymentId) throws MPException, MPApiException {
        Payment payment = paymentGateway.getPaymentDetails(paymentId);

        String status = payment.getStatus();
<<<<<<< HEAD
        String userEmail = payment.getExternalReference();  // CAMBIO: userEmail desde externalReference
        
        // CAMBIO: planCode desde los items del pago
        String planCode = null;
        if (payment.getAdditionalInfo() != null && 
            payment.getAdditionalInfo().getItems() != null && 
            !payment.getAdditionalInfo().getItems().isEmpty()) {
            planCode = payment.getAdditionalInfo().getItems().get(0).getId();
        }
=======
        String planCode = payment.getExternalReference();
        String userEmail = payment.getPayer().getEmail();
>>>>>>> 6cfddf8 (feat: relacion de usuarios con suscripcion)

        System.out.printf("Procesando Webhook de PAGO. ID Pago MP: %s, Plan: %s, Email: %s, Status: %s%n",
                paymentId, planCode, userEmail, status);

        if ("approved".equals(status)) {
            try {
<<<<<<< HEAD
                if (planCode == null) {
                    System.err.println("Error: No se pudo obtener el plan code del pago");
                    return;
                }
=======
>>>>>>> 6cfddf8 (feat: relacion de usuarios con suscripcion)
                upgradeUserSubscription(userEmail, planCode);
                System.out.println("Suscripción actualizada a " + planCode + " para usuario: " + userEmail);
            } catch (SubscriptionNotFoundException e) {
                System.err.println("Error: No se encontró suscripción para el usuario: " + userEmail);
            }
        } else if ("rejected".equals(status) || "cancelled".equals(status)) {
            System.out.println("Pago REJECTED/CANCELLED para: " + userEmail);
        }
    }
    
    private void upgradeUserSubscription(String userEmail, String planCode) throws SubscriptionNotFoundException {
        SubscriptionModel newPlan = subscriptionRepository.getByPlanCode(planCode);
        UserSubscriptionModel userSub = userSubscriptionRepository.findByUserEmail(userEmail);
        
        // Actualizar plan
        userSub.setPlanCode(newPlan.getPlanCode());
        userSub.setMaxCombinations(parseLimitFromFeature(newPlan.getFeature2()));
        userSub.setMaxFavorites(parseLimitFromFeature(newPlan.getFeature1()));
        userSub.setMaxModels(parseLimitFromFeature(newPlan.getFeature3()));
        userSub.setSubscriptionStart(LocalDateTime.now());
        userSub.setSubscriptionEnd(LocalDateTime.now().plusMonths(1));
        
        // Resetear contadores al cambiar de plan
        userSub.setCombinationsUsed(0);
        userSub.setModelsGenerated(0);
        // No resetear favoritesCount - es el contador real de BD
        
        userSubscriptionRepository.update(userSub);
    }
    
    private Integer parseLimitFromFeature(String feature) {
        if (feature == null) {
            return null;
        }
        
        if (feature.toLowerCase().contains("ilimitado") || 
            feature.toLowerCase().contains("ilimitada")) {
            return null;
        }
        
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(feature);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        
        return null;
    }
}