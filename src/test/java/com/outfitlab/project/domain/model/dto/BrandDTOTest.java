package com.outfitlab.project.domain.model.dto;

import com.outfitlab.project.domain.model.BrandModel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BrandDTOTest {

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void givenValidDataWhenCreateWithConstructorThenAllFieldsAreSet() {
        // GIVEN
        String codigoMarca = "NIKE-001";
        String nombre = "Nike";
        String logoUrl = "http://logo.url/nike.png";

        // WHEN
        BrandDTO dto = new BrandDTO(codigoMarca, nombre, logoUrl);

        // THEN
        thenDtoHasCorrectFields(dto, codigoMarca, nombre, logoUrl);
    }

    @Test
    void givenEmptyConstructorWhenCreateThenFieldsAreNull() {
        // WHEN
        BrandDTO dto = new BrandDTO();

        // THEN
        assertNull(dto.getCodigoMarca());
        assertNull(dto.getNombre());
        assertNull(dto.getLogoUrl());
    }

    // ========== CONVERSION TESTS ==========

    @Test
    void givenValidBrandModelWhenConvertToDtoThenAllFieldsAreMapped() {
        // GIVEN
        BrandModel model = givenValidBrandModel();

        // WHEN
        BrandDTO dto = BrandDTO.convertModelToDTO(model);

        // THEN
        thenDtoMatchesModel(dto, model);
    }

    @Test
    void givenListOfBrandModelsWhenConvertToListDtoThenAllItemsAreMapped() {
        // GIVEN
        List<BrandModel> models = givenListOfBrandModels();

        // WHEN
        List<BrandDTO> dtos = BrandDTO.convertListModelToListDTO(models);

        // THEN
        thenListHasCorrectSize(dtos, 3);
        thenDtoMatchesModel(dtos.get(0), models.get(0));
        thenDtoMatchesModel(dtos.get(1), models.get(1));
        thenDtoMatchesModel(dtos.get(2), models.get(2));
    }

    @Test
    void givenEmptyListWhenConvertToListDtoThenReturnEmptyList() {
        // GIVEN
        List<BrandModel> emptyList = Collections.emptyList();

        // WHEN
        List<BrandDTO> dtos = BrandDTO.convertListModelToListDTO(emptyList);

        // THEN
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    // ========== SETTER/GETTER TESTS ==========

    @Test
    void givenDtoWhenSetCodigoMarcaThenGetReturnsCorrectValue() {
        // GIVEN
        BrandDTO dto = new BrandDTO();
        String codigo = "ADIDAS-001";

        // WHEN
        dto.setCodigoMarca(codigo);

        // THEN
        assertEquals(codigo, dto.getCodigoMarca());
    }

    @Test
    void givenDtoWhenSetNombreThenGetReturnsCorrectValue() {
        // GIVEN
        BrandDTO dto = new BrandDTO();
        String nombre = "Adidas";

        // WHEN
        dto.setNombre(nombre);

        // THEN
        assertEquals(nombre, dto.getNombre());
    }

    @Test
    void givenDtoWhenSetLogoUrlThenGetReturnsCorrectValue() {
        // GIVEN
        BrandDTO dto = new BrandDTO();
        String logoUrl = "http://logo.url/adidas.png";

        // WHEN
        dto.setLogoUrl(logoUrl);

        // THEN
        assertEquals(logoUrl, dto.getLogoUrl());
    }

    // ========== HELPER METHODS (GIVEN) ==========

    private BrandModel givenValidBrandModel() {
        BrandModel model = new BrandModel();
        model.setCodigoMarca("NIKE-001");
        model.setNombre("Nike");
        model.setLogoUrl("http://logo.url/nike.png");
        return model;
    }

    private List<BrandModel> givenListOfBrandModels() {
        BrandModel brand1 = new BrandModel();
        brand1.setCodigoMarca("NIKE-001");
        brand1.setNombre("Nike");
        brand1.setLogoUrl("http://logo.url/nike.png");

        BrandModel brand2 = new BrandModel();
        brand2.setCodigoMarca("ADIDAS-001");
        brand2.setNombre("Adidas");
        brand2.setLogoUrl("http://logo.url/adidas.png");

        BrandModel brand3 = new BrandModel();
        brand3.setCodigoMarca("PUMA-001");
        brand3.setNombre("Puma");
        brand3.setLogoUrl("http://logo.url/puma.png");

        return Arrays.asList(brand1, brand2, brand3);
    }

    // ========== HELPER METHODS (THEN) ==========

    private void thenDtoHasCorrectFields(BrandDTO dto, String expectedCodigo, String expectedNombre,
            String expectedLogoUrl) {
        assertEquals(expectedCodigo, dto.getCodigoMarca());
        assertEquals(expectedNombre, dto.getNombre());
        assertEquals(expectedLogoUrl, dto.getLogoUrl());
    }

    private void thenDtoMatchesModel(BrandDTO dto, BrandModel model) {
        assertEquals(model.getCodigoMarca(), dto.getCodigoMarca());
        assertEquals(model.getNombre(), dto.getNombre());
        assertEquals(model.getLogoUrl(), dto.getLogoUrl());
    }

    private void thenListHasCorrectSize(List<BrandDTO> dtos, int expectedSize) {
        assertNotNull(dtos);
        assertEquals(expectedSize, dtos.size());
    }
}
