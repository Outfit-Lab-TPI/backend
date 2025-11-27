package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.helper.CodeFormatter;
import com.outfitlab.project.domain.useCases.garment.GetGarmentRecomendation;
import com.outfitlab.project.domain.useCases.recomendations.DeleteRecomendationByPrimaryAndSecondaryGarmentCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RecomendationController {

    private final GetGarmentRecomendation getGarmentRecomendation;
    private final DeleteRecomendationByPrimaryAndSecondaryGarmentCode deleteRecomendationByPrimaryAndSecondaryGarmentCode;

    public RecomendationController(GetGarmentRecomendation getGarmentRecomendation, DeleteRecomendationByPrimaryAndSecondaryGarmentCode deleteRecomendationByPrimaryAndSecondaryGarmentCode) {
        this.getGarmentRecomendation = getGarmentRecomendation;
        this.deleteRecomendationByPrimaryAndSecondaryGarmentCode = deleteRecomendationByPrimaryAndSecondaryGarmentCode;
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

    @DeleteMapping("/garment-recomendation/delete")
    public ResponseEntity<?> deleteRecomendation(@RequestParam String garmentCodePrimary, @RequestParam String garmentCodeSecondary, @RequestParam String type) {
        try {
            return ResponseEntity.ok(this.deleteRecomendationByPrimaryAndSecondaryGarmentCode.execute(garmentCodePrimary, garmentCodeSecondary, type));
        } catch (Exception e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }
}
