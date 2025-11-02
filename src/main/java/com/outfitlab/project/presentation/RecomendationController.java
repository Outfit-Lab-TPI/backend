package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.useCases.garment.GetGarmentRecomendation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RecomendationController {

    private final GetGarmentRecomendation getGarmentRecomendation;

    public RecomendationController(GetGarmentRecomendation getGarmentRecomendation) {
        this.getGarmentRecomendation = getGarmentRecomendation;
    }


    @GetMapping("/garment-recomendation/{garmentCode}")
    public ResponseEntity<?> getRecomendations(@PathVariable String garmentCode) {
        try {
            return ResponseEntity.ok(this.getGarmentRecomendation.execute(garmentCode));
        } catch (Exception e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }
}
