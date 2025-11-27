package com.outfitlab.project.domain.model.dto;

import com.outfitlab.project.domain.model.ClimaModel;
import com.outfitlab.project.domain.model.ColorModel;
import com.outfitlab.project.domain.model.OcasionModel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationCategoriesDTOTest {

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void givenValidDataWhenCreateWithConstructorThenAllFieldsAreSet() {
        // GIVEN
        List<ClimaModel> climas = givenListOfClimas();
        List<OcasionModel> ocasiones = givenListOfOcasiones();
        List<ColorModel> colores = givenListOfColores();

        // WHEN
        RecommendationCategoriesDTO dto = new RecommendationCategoriesDTO(climas, ocasiones, colores);

        // THEN
        thenDtoHasCorrectFields(dto, climas, ocasiones, colores);
    }

    // ========== SETTER/GETTER TESTS ==========

    @Test
    void givenDtoWhenSetClimasThenGetReturnsCorrectValue() {
        // GIVEN
        List<ClimaModel> climas = givenListOfClimas();
        RecommendationCategoriesDTO dto = givenBasicDTO();

        // WHEN
        dto.setClimas(climas);

        // THEN
        assertEquals(climas, dto.getClimas());
        assertEquals(3, dto.getClimas().size());
    }

    @Test
    void givenDtoWhenSetOcasionesThenGetReturnsCorrectValue() {
        // GIVEN
        List<OcasionModel> ocasiones = givenListOfOcasiones();
        RecommendationCategoriesDTO dto = givenBasicDTO();

        // WHEN
        dto.setOcasiones(ocasiones);

        // THEN
        assertEquals(ocasiones, dto.getOcasiones());
        assertEquals(2, dto.getOcasiones().size());
    }

    @Test
    void givenDtoWhenSetColoresThenGetReturnsCorrectValue() {
        // GIVEN
        List<ColorModel> colores = givenListOfColores();
        RecommendationCategoriesDTO dto = givenBasicDTO();

        // WHEN
        dto.setColores(colores);

        // THEN
        assertEquals(colores, dto.getColores());
        assertEquals(4, dto.getColores().size());
    }

    // ========== BUSINESS LOGIC TESTS ==========

    @Test
    void givenDtoWithAllCategoriesWhenGetThenAllListsArePopulated() {
        // GIVEN
        List<ClimaModel> climas = givenListOfClimas();
        List<OcasionModel> ocasiones = givenListOfOcasiones();
        List<ColorModel> colores = givenListOfColores();

        // WHEN
        RecommendationCategoriesDTO dto = new RecommendationCategoriesDTO(climas, ocasiones, colores);

        // THEN
        assertNotNull(dto.getClimas());
        assertNotNull(dto.getOcasiones());
        assertNotNull(dto.getColores());
        assertFalse(dto.getClimas().isEmpty());
        assertFalse(dto.getOcasiones().isEmpty());
        assertFalse(dto.getColores().isEmpty());
    }

    @Test
    void givenDtoWithEmptyListsWhenGetThenListsAreEmpty() {
        // GIVEN
        List<ClimaModel> emptyClimas = Collections.emptyList();
        List<OcasionModel> emptyOcasiones = Collections.emptyList();
        List<ColorModel> emptyColores = Collections.emptyList();

        // WHEN
        RecommendationCategoriesDTO dto = new RecommendationCategoriesDTO(emptyClimas, emptyOcasiones, emptyColores);

        // THEN
        assertTrue(dto.getClimas().isEmpty());
        assertTrue(dto.getOcasiones().isEmpty());
        assertTrue(dto.getColores().isEmpty());
    }

    // ========== HELPER METHODS (GIVEN) ==========

    private List<ClimaModel> givenListOfClimas() {
        ClimaModel calido = new ClimaModel();
        calido.setNombre("Calido");

        ClimaModel frio = new ClimaModel();
        frio.setNombre("Frio");

        ClimaModel templado = new ClimaModel();
        templado.setNombre("Templado");

        return Arrays.asList(calido, frio, templado);
    }

    private List<OcasionModel> givenListOfOcasiones() {
        OcasionModel casual = new OcasionModel();
        casual.setNombre("Casual");

        OcasionModel formal = new OcasionModel();
        formal.setNombre("Formal");

        return Arrays.asList(casual, formal);
    }

    private List<ColorModel> givenListOfColores() {
        ColorModel rojo = new ColorModel();
        rojo.setNombre("Rojo");

        ColorModel azul = new ColorModel();
        azul.setNombre("Azul");

        ColorModel verde = new ColorModel();
        verde.setNombre("Verde");

        ColorModel negro = new ColorModel();
        negro.setNombre("Negro");

        return Arrays.asList(rojo, azul, verde, negro);
    }

    private RecommendationCategoriesDTO givenBasicDTO() {
        return new RecommendationCategoriesDTO(null, null, null);
    }

    // ========== HELPER METHODS (THEN) ==========

    private void thenDtoHasCorrectFields(RecommendationCategoriesDTO dto, List<ClimaModel> expectedClimas,
            List<OcasionModel> expectedOcasiones, List<ColorModel> expectedColores) {
        assertEquals(expectedClimas, dto.getClimas());
        assertEquals(expectedOcasiones, dto.getOcasiones());
        assertEquals(expectedColores, dto.getColores());
    }
}
