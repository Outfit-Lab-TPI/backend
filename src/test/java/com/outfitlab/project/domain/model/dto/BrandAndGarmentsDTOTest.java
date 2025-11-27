package com.outfitlab.project.domain.model.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BrandAndGarmentsDTOTest {

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void givenValidDataWhenCreateWithConstructorThenAllFieldsAreSet() {
        // GIVEN
        BrandDTO brand = givenBrandDTO();
        PageDTO<GarmentDTO> topGarments = givenPageOfTopGarments();
        PageDTO<GarmentDTO> bottomGarments = givenPageOfBottomGarments();

        // WHEN
        BrandAndGarmentsDTO dto = new BrandAndGarmentsDTO(brand, topGarments, bottomGarments);

        // THEN
        thenDtoHasCorrectFields(dto, brand, topGarments, bottomGarments);
    }

    // ========== SETTER/GETTER TESTS ==========

    @Test
    void givenDtoWhenSetBrandDtoThenGetReturnsCorrectValue() {
        // GIVEN
        BrandDTO brand = givenBrandDTO();
        BrandAndGarmentsDTO dto = givenBasicBrandAndGarmentsDTO();

        // WHEN
        dto.setBrandDTO(brand);

        // THEN
        assertEquals(brand, dto.getBrandDTO());
    }

    @Test
    void givenDtoWhenSetGarmentTopThenGetReturnsCorrectValue() {
        // GIVEN
        PageDTO<GarmentDTO> topGarments = givenPageOfTopGarments();
        BrandAndGarmentsDTO dto = givenBasicBrandAndGarmentsDTO();

        // WHEN
        dto.setGarmentTop(topGarments);

        // THEN
        assertEquals(topGarments, dto.getGarmentTop());
    }

    @Test
    void givenDtoWhenSetGarmentBottomThenGetReturnsCorrectValue() {
        // GIVEN
        PageDTO<GarmentDTO> bottomGarments = givenPageOfBottomGarments();
        BrandAndGarmentsDTO dto = givenBasicBrandAndGarmentsDTO();

        // WHEN
        dto.setGarmentBottom(bottomGarments);

        // THEN
        assertEquals(bottomGarments, dto.getGarmentBottom());
    }

    // ========== INTEGRATION TESTS ==========

    @Test
    void givenBrandWithGarmentsWhenCreateDtoThenStructureIsCorrect() {
        // GIVEN
        BrandDTO brand = givenBrandDTO();
        PageDTO<GarmentDTO> topGarments = givenPageOfTopGarments();
        PageDTO<GarmentDTO> bottomGarments = givenPageOfBottomGarments();

        // WHEN
        BrandAndGarmentsDTO dto = new BrandAndGarmentsDTO(brand, topGarments, bottomGarments);

        // THEN
        assertNotNull(dto.getBrandDTO());
        assertNotNull(dto.getGarmentTop());
        assertNotNull(dto.getGarmentBottom());
        assertEquals("Nike", dto.getBrandDTO().getNombre());
        assertEquals(2, dto.getGarmentTop().getContent().size());
        assertEquals(2, dto.getGarmentBottom().getContent().size());
    }

    @Test
    void givenBrandWithEmptyGarmentsWhenCreateDtoThenPagesAreEmpty() {
        // GIVEN
        BrandDTO brand = givenBrandDTO();
        PageDTO<GarmentDTO> emptyTop = givenEmptyPage();
        PageDTO<GarmentDTO> emptyBottom = givenEmptyPage();

        // WHEN
        BrandAndGarmentsDTO dto = new BrandAndGarmentsDTO(brand, emptyTop, emptyBottom);

        // THEN
        assertNotNull(dto.getBrandDTO());
        assertTrue(dto.getGarmentTop().getContent().isEmpty());
        assertTrue(dto.getGarmentBottom().getContent().isEmpty());
    }

    // ========== HELPER METHODS (GIVEN) ==========

    private BrandDTO givenBrandDTO() {
        return new BrandDTO("NIKE-001", "Nike", "http://logo.url/nike.png");
    }

    private PageDTO<GarmentDTO> givenPageOfTopGarments() {
        GarmentDTO shirt = new GarmentDTO("Shirt", "superior", "http://img1.jpg", "SHIRT-001", "Nike", "Rojo",
                "Calido");
        GarmentDTO jacket = new GarmentDTO("Jacket", "superior", "http://img2.jpg", "JACKET-001", "Nike", "Azul",
                "Frio");
        List<GarmentDTO> content = Arrays.asList(shirt, jacket);
        return new PageDTO<>(content, 0, 10, 2L, 1, true);
    }

    private PageDTO<GarmentDTO> givenPageOfBottomGarments() {
        GarmentDTO pants = new GarmentDTO("Pants", "inferior", "http://img3.jpg", "PANTS-001", "Nike", "Negro",
                "Templado");
        GarmentDTO shorts = new GarmentDTO("Shorts", "inferior", "http://img4.jpg", "SHORTS-001", "Nike", "Blanco",
                "Calido");
        List<GarmentDTO> content = Arrays.asList(pants, shorts);
        return new PageDTO<>(content, 0, 10, 2L, 1, true);
    }

    private PageDTO<GarmentDTO> givenEmptyPage() {
        return new PageDTO<>(Collections.emptyList(), 0, 10, 0L, 0, true);
    }

    private BrandAndGarmentsDTO givenBasicBrandAndGarmentsDTO() {
        return new BrandAndGarmentsDTO(null, null, null);
    }

    // ========== HELPER METHODS (THEN) ==========

    private void thenDtoHasCorrectFields(BrandAndGarmentsDTO dto, BrandDTO expectedBrand,
            PageDTO<GarmentDTO> expectedTop, PageDTO<GarmentDTO> expectedBottom) {
        assertEquals(expectedBrand, dto.getBrandDTO());
        assertEquals(expectedTop, dto.getGarmentTop());
        assertEquals(expectedBottom, dto.getGarmentBottom());
    }
}
