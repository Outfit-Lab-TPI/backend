package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.interfaces.repositories.ClimaRepository;
import com.outfitlab.project.domain.interfaces.repositories.ColorRepository;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.interfaces.repositories.OcacionRepository;
import com.outfitlab.project.domain.model.ColorModel;
import com.outfitlab.project.domain.model.dto.RecommendationCategoriesDTO;
import com.outfitlab.project.domain.model.ClimaModel;
import com.outfitlab.project.domain.model.OcasionModel;
import com.outfitlab.project.domain.useCases.recomendations.GetAllClima;
import com.outfitlab.project.domain.useCases.recomendations.GetAllColors;
import com.outfitlab.project.domain.useCases.recomendations.GetAllOcacion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final GetAllClima getAllClima;
    private final GetAllOcacion getAllOcacion;
    private final GetAllColors getAllColors;

    public CategoryController(GetAllClima getAllClima, GetAllOcacion getAllOcacion, GetAllColors getAllColors) {
        this.getAllClima = getAllClima;
        this.getAllOcacion = getAllOcacion;
        this.getAllColors = getAllColors;
    }

    @GetMapping(value = "/recommendation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecommendationCategoriesDTO> getRecommendationCategories() {

        List<ClimaModel> climas = this.getAllClima.execute();
        List<OcasionModel> ocasiones = this.getAllOcacion.execute();
        List<ColorModel> colores = this.getAllColors.execute();

        RecommendationCategoriesDTO categories = new RecommendationCategoriesDTO(climas, ocasiones, colores);

        return ResponseEntity.ok(categories);
    }
}