package com.outfitlab.project.presentation;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import com.outfitlab.project.domain.model.UserSubscriptionModel;
import com.outfitlab.project.domain.useCases.subscription.CreateMercadoPagoPreference;
import com.outfitlab.project.domain.useCases.subscription.GetAllSubscription;
import com.outfitlab.project.domain.useCases.subscription.ProcessPaymentNotification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;

class SubscriptionRequest {
    private String planId;
    private String userEmail;
    private BigDecimal price;
    private String currency;

    public String getPlanId() { return planId; }
    public void setPlanId(String planId) { this.planId = planId; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

}

@RestController
@RequestMapping("/api/mp")
public class SubscriptionController {

    private final CreateMercadoPagoPreference createPreferenceUseCase;
    private final ProcessPaymentNotification processNotificationUseCase;
    private final GetAllSubscription getAllSubscription;
    private final UserSubscriptionRepository userSubscriptionRepository;

    public SubscriptionController(
            CreateMercadoPagoPreference createPreferenceUseCase,
            ProcessPaymentNotification processNotificationUseCase,
            GetAllSubscription getAllSubscription,
            UserSubscriptionRepository userSubscriptionRepository)
    {
        this.createPreferenceUseCase = createPreferenceUseCase;
        this.processNotificationUseCase = processNotificationUseCase;
        this.getAllSubscription = getAllSubscription;
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @PostMapping("/crear-suscripcion")
    public ResponseEntity<Map<String, String>> createPreference(@RequestBody SubscriptionRequest request) {

        if (request.getPlanId() == null || request.getUserEmail() == null || request.getPrice() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Faltan planId, userEmail o price."));
        }

        try {
            String initPointUrl = createPreferenceUseCase.execute(
                    request.getPlanId(),
                    request.getUserEmail(),
                    request.getPrice(),
                    request.getCurrency() != null ? request.getCurrency() : "ARS"
            );

            Map<String, String> response = new HashMap<>();
            response.put("initPoint", initPointUrl);
            return ResponseEntity.ok(response);

        } catch (MPException | MPApiException e) {
            e.printStackTrace();
            System.err.println("Error de API de Mercado Pago: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Error al crear la preferencia en Mercado Pago."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error interno del servidor."));
        }
    }

    @PostMapping("/webhooks")
    public ResponseEntity<String> handleMercadoPagoWebhook(
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "topic", required = false) String topic)
    {
        if ("payment".equals(topic) && id != null) {
            try {
                Long paymentId = Long.parseLong(id);

                processNotificationUseCase.execute(paymentId);

                return ResponseEntity.ok("Notification processed successfully.");
            } catch (NumberFormatException e) {
                System.err.println("Error: El ID del Webhook no es un número (Long). ID: " + id);
                return ResponseEntity.badRequest().body("ID inválido.");
            } catch (MPException | MPApiException e) {
                System.err.println("Error procesando Webhook de Pago: " + e.getMessage());
                return ResponseEntity.status(500).body("Error processing notification.");
            }
        }

        return ResponseEntity.ok("Notification received, not a relevant topic.");
    }


    @GetMapping("/subscriptions")
    public ResponseEntity<?> getSubscriptions() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("data", this.getAllSubscription.execute());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/user-subscription")
    public ResponseEntity<?> getUserSubscription(@RequestParam String email) {
        try {
            UserSubscriptionModel subscription = userSubscriptionRepository.findByUserEmail(email);
            
            Map<String, Object> response = new HashMap<>();
            response.put("planCode", subscription.getPlanCode());
            response.put("status", subscription.getStatus());
            response.put("usage", Map.of(
                "combinations", Map.of(
                    "used", subscription.getCombinationsUsed(),
                    "max", subscription.getMaxCombinations()
                ),
                "favorites", Map.of(
                    "count", subscription.getFavoritesCount(),
                    "max", subscription.getMaxFavorites()
                ),
                "models", Map.of(
                    "generated", subscription.getModelsGenerated(),
                    "max", subscription.getMaxModels()
                )
            ));
            
            return ResponseEntity.ok(response);
        } catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}