package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.exceptions.PredictionFailedException;
import com.outfitlab.project.domain.exceptions.PredictionTimeoutException;
import com.outfitlab.project.domain.service.FashnService;
import com.outfitlab.project.presentation.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fashion")
public class FashnController {

    private final FashnService fashnService;

    public FashnController(FashnService tryOnService) {
        this.fashnService = tryOnService;
    }

    @PostMapping("/combinar-prendas")
    public ResponseEntity<GeneratedResponse> combine(@RequestBody CombineRequest req) {
        System.out.println(req.toString());

        try {

            GeneratedResponse resp = new GeneratedResponse("OK", fashnService.combine(req), null);

            return ResponseEntity.ok(resp);
        } catch (PredictionFailedException e) {
            System.out.println(e.getMessage());
            GeneratedResponse resp = new GeneratedResponse("FAILED", null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(resp);
        } catch (PredictionTimeoutException e) {
            System.out.println(e.getMessage());
            GeneratedResponse resp = new GeneratedResponse("TIMEOUT", null, e.getMessage());
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(resp);
        } catch (FashnApiException e) {
            System.out.println(e.getMessage());
            GeneratedResponse resp = new GeneratedResponse("ERROR", null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(resp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            GeneratedResponse resp = new GeneratedResponse("ERROR", null, "Error inesperado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }
}

