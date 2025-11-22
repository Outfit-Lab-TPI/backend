package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.model.dto.ConjuntoDTO;
import com.outfitlab.project.domain.useCases.garment.GetGarmentRecomendationByText;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/outfits")
public class GarmentRecommendationAIController {

    private final GetGarmentRecomendationByText recomendationUseCase;

    public GarmentRecommendationAIController(GetGarmentRecomendationByText recomendationUseCase) {
        this.recomendationUseCase = recomendationUseCase;
    }

    @PostMapping("/recommend")
    public ResponseEntity<List<ConjuntoDTO>> recommendOutfit(@RequestBody RecommendationRequest request) {

        List<ConjuntoDTO> outfits = recomendationUseCase.execute(
                request.getPeticionUsuario(),
                request.getIdUsuario()
        );

        if (outfits.isEmpty() || (outfits.size() == 1 && outfits.get(0).getPrendas().isEmpty())) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(outfits);
    }

    static class RecommendationRequest {
        private String peticionUsuario;
        private String idUsuario;

        public String getPeticionUsuario() { return peticionUsuario; }
        public void setPeticionUsuario(String peticionUsuario) { this.peticionUsuario = peticionUsuario; }
        public String getIdUsuario() { return idUsuario; }
        public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    }
}
