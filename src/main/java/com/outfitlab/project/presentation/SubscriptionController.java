package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.InvalidPlanException;
import com.outfitlab.project.domain.exceptions.PaymentFailedException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.PaymentPreferenceModel;
import com.outfitlab.project.domain.useCases.subscription.CreateSubscriptionPaymentPreference;
import com.outfitlab.project.domain.useCases.subscription.GetSubscriptionStatus;
import com.outfitlab.project.domain.useCases.subscription.ProcessSubscriptionPayment;
import com.outfitlab.project.presentation.dto.SubscriptionStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * DTO para crear suscripción (mantiene compatibilidad con frontend).
 */
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

/**
 * Controller para suscripciones premium con MercadoPago.
 * 
 * Endpoints:
 * - POST /api/mp/purchase/{planCode} - Crear preferencia de pago
 * - POST /api/mp/webhooks - Webhook de MercadoPago
 * - GET /api/mp/status/{userId} - Estado de suscripción
 */
@RestController
@RequestMapping("/api/mp")
public class SubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    private final CreateSubscriptionPaymentPreference createPaymentPreference;
    private final ProcessSubscriptionPayment processPayment;
    private final GetSubscriptionStatus getSubscriptionStatus;

    public SubscriptionController(CreateSubscriptionPaymentPreference createPaymentPreference,
                                    ProcessSubscriptionPayment processPayment,
                                    GetSubscriptionStatus getSubscriptionStatus) {
        this.createPaymentPreference = createPaymentPreference;
        this.processPayment = processPayment;
        this.getSubscriptionStatus = getSubscriptionStatus;
    }

    /**
     * Crear preferencia de pago para un plan de suscripción.
     * 
     * @param planCode Código del plan (PREMIUM_MONTHLY, PREMIUM_YEARLY)
     * @param request DTO con userEmail
     * @return URL de pago (initPoint) o error
     */
    @PostMapping("/purchase/{planCode}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> createPreference(
            @PathVariable String planCode,
            @RequestBody SubscriptionRequest request) {

        try {
            logger.info("POST /api/mp/purchase/{} - User Email: {}", planCode, request.getUserEmail());

            if (request.getUserEmail() == null || request.getUserEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "UserEmail es requerido"));
            }

            String initPoint = createPaymentPreference.execute(
                planCode,
                request.getUserEmail()
            );

            Map<String, String> response = new HashMap<>();
            response.put("initPoint", initPoint);
            return ResponseEntity.ok(response);

        } catch (InvalidPlanException e) {
            logger.error("Plan inválido: {}", planCode);
            return ResponseEntity.status(400).body(Map.of(
                "error", "INVALID_PLAN",
                "message", e.getMessage()
            ));

        } catch (Exception e) {
            logger.error("Error al crear preferencia de pago", e);
            return ResponseEntity.status(500).body(Map.of(
                "error", "INTERNAL_ERROR",
                "message", "Error al crear preferencia: " + e.getMessage()
            ));
        }
    }

    /**
     * Webhook de MercadoPago para notificaciones de pago.
     * 
     * @param id ID del pago
     * @param topic Tipo de notificación
     * @return Confirmación de procesamiento
     */
    @PostMapping("/webhooks")
    public ResponseEntity<String> handleMercadoPagoWebhook(
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "topic", required = false) String topic) {

        logger.info("Webhook recibido. Topic: {}, ID: {}", topic, id);

        if (!"payment".equals(topic)) {
            logger.info("Webhook ignorado (topic no es 'payment')");
            return ResponseEntity.ok("Notification received, not a payment topic.");
        }

        if (id == null || id.trim().isEmpty()) {
            logger.error("Webhook sin ID de pago");
            return ResponseEntity.badRequest().body("Payment ID is required");
        }

        try {
            processPayment.execute(id);

            logger.info("Pago procesado exitosamente. Payment ID: {}", id);
            return ResponseEntity.ok("Payment notification processed successfully.");

        } catch (PaymentFailedException e) {
            logger.warn("Pago no aprobado: {}", e.getMessage());
            return ResponseEntity.ok("Payment not approved, notification logged.");

        } catch (Exception e) {
            logger.error("Error procesando webhook", e);
            return ResponseEntity.status(500).body("Error processing payment notification");
        }
    }

    /**
     * Obtener estado de suscripción de un usuario.
     * 
     * @param userId ID del usuario
     * @return Estado de suscripción y límites de favoritos
     */
    @GetMapping("/status/{userId}")
    public ResponseEntity<?> getStatus(@PathVariable Long userId) {
        try {
            logger.info("GET /api/mp/status/{}", userId);
            
            SubscriptionStatusResponse status = getSubscriptionStatus.execute(userId);
            
            return ResponseEntity.ok(status);
            
        } catch (UserNotFoundException e) {
            logger.error("Usuario no encontrado: {}", userId);
            return ResponseEntity.status(404).body(Map.of(
                "error", "USER_NOT_FOUND",
                "message", e.getMessage()
            ));
            
        } catch (Exception e) {
            logger.error("Error al obtener estado de suscripción", e);
            return ResponseEntity.status(500).body(Map.of(
                "error", "INTERNAL_ERROR",
                "message", "Error al obtener estado: " + e.getMessage()
            ));
        }
    }    // ========== ENDPOINT LEGACY (Mantener compatibilidad con frontend antiguo) ==========

    /**
     * Endpoint legacy para compatibilidad con frontend antiguo.
     * @deprecated Use /purchase/{planCode} instead
     */
    @Deprecated
    @PostMapping("/crear-suscripcion")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Map<String, String>> createPreferenceLegacy(@RequestBody SubscriptionRequest request) {
        logger.warn("Endpoint legacy /crear-suscripcion usado. Migrar a /purchase/{planCode}");

        if (request.getPlanId() == null || request.getUserEmail() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "planId y userEmail son requeridos"));
        }

        try {
            String initPoint = createPaymentPreference.execute(
                request.getPlanId(),
                request.getUserEmail()
            );

            Map<String, String> response = new HashMap<>();
            response.put("initPoint", initPoint);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error en endpoint legacy", e);
            return ResponseEntity.status(500).body(Map.of("error", "Error interno del servidor"));
        }
    }
}
