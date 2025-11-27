package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.model.ClimaModel;
import com.outfitlab.project.domain.model.ColorModel;
import com.outfitlab.project.domain.model.OcasionModel;
import com.outfitlab.project.domain.model.dto.RecommendationCategoriesDTO;
import com.outfitlab.project.domain.useCases.recomendations.GetAllClima;
import com.outfitlab.project.domain.useCases.recomendations.GetAllColors;
import com.outfitlab.project.domain.useCases.recomendations.GetAllOcacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryControllerTest {

    private GetAllClima getAllClima;
    private GetAllOcacion getAllOcacion;
    private GetAllColors getAllColors;

    private CategoryController controller;

    @BeforeEach
    void setUp() {
        getAllClima = Mockito.mock(GetAllClima.class);
        getAllOcacion = Mockito.mock(GetAllOcacion.class);
        getAllColors = Mockito.mock(GetAllColors.class);

        controller = new CategoryController(getAllClima, getAllOcacion, getAllColors);
    }

    @Test
    void shouldReturnRecommendationCategories() {

        List<ClimaModel> climas = givenClimas();
        List<OcasionModel> ocasiones = givenOcasiones();
        List<ColorModel> colores = givenColores();

        mockUseCases(climas, ocasiones, colores);

        ResponseEntity<RecommendationCategoriesDTO> response = whenCallingEndpoint();

        thenResponseIsCorrect(response, climas, ocasiones, colores);
    }

    private List<ClimaModel> givenClimas() {
        return Arrays.asList(new ClimaModel(1L, "calido"), new ClimaModel(2L,"frio"));
    }

    private List<OcasionModel> givenOcasiones() {
        return Arrays.asList(new OcasionModel(1L,"fiesta"), new OcasionModel(2L,"trabajo"));
    }

    private List<ColorModel> givenColores() {
        return Arrays.asList(new ColorModel(1L, "rojo", 1), new ColorModel(2L, "azul", 1));
    }

    private void mockUseCases(List<ClimaModel> climas, List<OcasionModel> ocasiones, List<ColorModel> colores) {
        when(getAllClima.execute()).thenReturn(climas);
        when(getAllOcacion.execute()).thenReturn(ocasiones);
        when(getAllColors.execute()).thenReturn(colores);
    }

    private ResponseEntity<RecommendationCategoriesDTO> whenCallingEndpoint() {
        return controller.getRecommendationCategories();
    }

    private void thenResponseIsCorrect(ResponseEntity<RecommendationCategoriesDTO> response, List<ClimaModel> climas, List<OcasionModel> ocasiones, List<ColorModel> colores) {
        assertEquals(200, response.getStatusCodeValue());
        RecommendationCategoriesDTO body = response.getBody();
        assertEquals(climas, body.getClimas());
        assertEquals(ocasiones, body.getOcasiones());
        assertEquals(colores, body.getColores());
    }
}
