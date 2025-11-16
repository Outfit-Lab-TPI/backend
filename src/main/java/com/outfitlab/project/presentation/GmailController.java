package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.useCases.gmail.SubscribeUser;
import com.outfitlab.project.presentation.dto.GmailDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/suscripcion")
public class GmailController {

    private final SubscribeUser subscribeUser;

    public GmailController(SubscribeUser subscribeUser) {
        this.subscribeUser = subscribeUser;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, String>> subscribeUser(@RequestBody GmailDTO request) {

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El campo email es obligatorio."));
        }

        try {
            // Llama al Caso de Uso de Dominio
            subscribeUser.execute(request.getEmail());

            return ResponseEntity.ok(Map.of("message", "Suscripción exitosa. Revisa tu correo."));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al procesar la suscripción: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Error interno al enviar el correo."));
        }
    }
}
