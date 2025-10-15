package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.fashion.*;
import com.outfitlab.project.infrastructure.TryOnService;
import com.outfitlab.project.presentation.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fashion")
public class TryOnController {

    private final TryOnService tryOnService;

    public TryOnController(TryOnService tryOnService) {
        this.tryOnService = tryOnService;
    }

    @PostMapping("/combinar-prendas")
    public ResponseEntity<GeneratedResponse> combine(@RequestBody CombineRequest req) {
        System.out.println(req.toString());
        try {
            String generatedUrl = tryOnService.combine(req);
            GeneratedResponse resp = new GeneratedResponse("OK", generatedUrl, null);
            return ResponseEntity.ok(resp);
        } catch (PredictionFailedException ex) {
            GeneratedResponse resp = new GeneratedResponse("FAILED", null, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(resp);
        } catch (PredictionTimeoutException ex) {
            GeneratedResponse resp = new GeneratedResponse("TIMEOUT", null, ex.getMessage());
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(resp);
        } catch (FashnApiException ex) {
            GeneratedResponse resp = new GeneratedResponse("ERROR", null, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(resp);
        } catch (Exception ex) {
            GeneratedResponse resp = new GeneratedResponse("ERROR", null, "Error inesperado: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }
}

