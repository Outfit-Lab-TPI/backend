package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.dto.RecommendationCategoriesDTO;
import com.outfitlab.project.domain.model.ClimaModel;
import com.outfitlab.project.domain.model.OcasionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final GarmentRepository garmentRepository;

    public CategoryController(GarmentRepository garmentRepository) {
        this.garmentRepository = garmentRepository;
    }

    @GetMapping("/recommendation")
    public ResponseEntity<RecommendationCategoriesDTO> getRecommendationCategories() {

        List<ClimaModel> climas = garmentRepository.findAllClimas();
        List<OcasionModel> ocasiones = garmentRepository.findAllOcasiones();

        RecommendationCategoriesDTO categories = new RecommendationCategoriesDTO(climas, ocasiones);

        return ResponseEntity.ok(categories);
    }
}