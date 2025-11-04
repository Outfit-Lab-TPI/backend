package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exception.InvalidPlanException;
import com.outfitlab.project.domain.exception.PaymentFailedException;
import com.outfitlab.project.domain.useCases.subscription.CreateSubscriptionPaymentPreference;
import com.outfitlab.project.domain.useCases.subscription.ProcessSubscriptionPayment;
import com.outfitlab.project.presentation.dto.PurchasePlanRequest;
import com.outfitlab.project.presentation.dto.SubscriptionStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mp")
public class SubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    private final CreateSubscriptionPaymentPreference createSubscriptionPaymentPreference;
    private final ProcessSubscriptionPayment processSubscriptionPayment;

    public SubscriptionController(
            CreateSubscriptionPaymentPreference createSubscriptionPaymentPreference,
            ProcessSubscriptionPayment processSubscriptionPayment
    ) {
        this.createSubscriptionPaymentPreference = createSubscriptionPaymentPreference;
        this.processSubscriptionPayment = processSubscriptionPayment;
    }

    /**
     * Endpoint para crear una preferencia de pago y obtener el link de MercadoPago.
     */
    @PostMapping("/purchase/{planCode}")
    public ResponseEntity<?> purchasePlan(
            @PathVariable String planCode,
            @RequestBody PurchasePlanRequest request
    ) {
        try {
            String initPoint = createSubscriptionPaymentPreference.execute(planCode, request.getUserEmail());
            return ResponseEntity.ok(initPoint);
        } catch (InvalidPlanException e) {
            logger.error("Plan inválido: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al crear preferencia de pago", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    /**
     * Endpoint para consultar el estado de suscripción de un usuario.
     */
    @GetMapping("/status/{userId}")
    public ResponseEntity<SubscriptionStatusResponse> getSubscriptionStatus(@PathVariable Long userId) {
        // TODO: Implementar GetUserSubscriptionStatus use case
        return ResponseEntity.ok(new SubscriptionStatusResponse());
    }

    /**
     * Webhook de MercadoPago para procesar notificaciones de pago.
     * MercadoPago envía: ?id=<payment_id>&topic=payment
     */
    @PostMapping("/webhooks")
    public ResponseEntity<String> handleMercadoPagoWebhook(
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "topic", required = false) String topic
    ) {
        logger.info("Webhook recibido - Topic: {}, ID: {}", topic, id);

        // Validar que sea una notificación de pago
        if (!"payment".equals(topic)) {
            logger.info("Topic no relevante: {}", topic);
            return ResponseEntity.ok("Notification received, not a payment topic.");
        }

        // Validar que el ID esté presente
        if (id == null || id.isEmpty()) {
            logger.error("ID de pago vacío en webhook");
            return ResponseEntity.badRequest().body("ID de pago requerido");
        }

        try {
            // Procesar el pago usando el Use Case
            processSubscriptionPayment.execute(id);
            
            logger.info("Pago procesado exitosamente: {}", id);
            return ResponseEntity.ok("Notification processed successfully.");
            
        } catch (PaymentFailedException e) {
            logger.error("Error al procesar pago {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al procesar pago: " + e.getMessage());
                    
        } catch (InvalidPlanException e) {
            logger.error("Plan inválido en pago {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Plan inválido: " + e.getMessage());
                    
        } catch (Exception e) {
            logger.error("Error inesperado al procesar webhook de pago {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing notification.");
        }
    }
}
