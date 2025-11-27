package com.outfitlab.project.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BrandModelTest {

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void givenEmptyConstructorWhenCreateThenFieldsAreNull() {
        // WHEN
        BrandModel brand = new BrandModel();

        // THEN
        assertNull(brand.getCodigoMarca());
        assertNull(brand.getNombre());
        assertNull(brand.getLogoUrl());
        assertNull(brand.getUrlSite());
        assertNull(brand.getCreatedAt());
        assertNull(brand.getUpdatedAt());
        assertNull(brand.getPrendas());
    }

    @Test
    void givenBasicDataWhenCreateWith3ParamsThenFieldsAreSet() {
        // GIVEN
        String codigo = "NIKE-001";
        String nombre = "Nike";
        String logoUrl = "http://logo.url/nike.png";

        // WHEN
        BrandModel brand = new BrandModel(codigo, nombre, logoUrl);

        // THEN
        assertEquals(codigo, brand.getCodigoMarca());
        assertEquals(nombre, brand.getNombre());
        assertEquals(logoUrl, brand.getLogoUrl());
        assertNull(brand.getUrlSite());
        assertNull(brand.getCreatedAt());
    }

    @Test
    void givenDataWithUrlSiteWhenCreateWith4ParamsThenAllFieldsAreSet() {
        // GIVEN
        String codigo = "ADIDAS-001";
        String nombre = "Adidas";
        String logoUrl = "http://logo.url/adidas.png";
        String urlSite = "https://adidas.com";

        // WHEN
        BrandModel brand = new BrandModel(codigo, nombre, logoUrl, urlSite);

        // THEN
        thenBrandHasBasicFields(brand, codigo, nombre, logoUrl);
        assertEquals(urlSite, brand.getUrlSite());
    }

    @Test
    void givenDataWithTimestampsWhenCreateWith5ParamsThenAllFieldsAreSet() {
        // GIVEN
        String codigo = "PUMA-001";
        String nombre = "Puma";
        String logoUrl = "http://logo.url/puma.png";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // WHEN
        BrandModel brand = new BrandModel(codigo, nombre, logoUrl, createdAt, updatedAt);

        // THEN
        thenBrandHasBasicFields(brand, codigo, nombre, logoUrl);
        assertEquals(createdAt, brand.getCreatedAt());
        assertEquals(updatedAt, brand.getUpdatedAt());
    }

    @Test
    void givenFullDataWhenCreateWith6ParamsThenAllFieldsAreSet() {
        // GIVEN
        String codigo = "REEBOK-001";
        String nombre = "Reebok";
        String logoUrl = "http://logo.url/reebok.png";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        List<PrendaModel> prendas = givenListOfPrendas();

        // WHEN
        BrandModel brand = new BrandModel(codigo, nombre, logoUrl, createdAt, updatedAt, prendas);

        // THEN
        thenBrandHasBasicFields(brand, codigo, nombre, logoUrl);
        assertEquals(createdAt, brand.getCreatedAt());
        assertEquals(updatedAt, brand.getUpdatedAt());
        assertEquals(prendas, brand.getPrendas());
        assertEquals(2, brand.getPrendas().size());
    }

    // ========== SETTER/GETTER TESTS ==========

    @Test
    void givenBrandWhenSetCodigoMarcaThenGetReturnsCorrectValue() {
        // GIVEN
        BrandModel brand = new BrandModel();
        String codigo = "TEST-001";

        // WHEN
        brand.setCodigoMarca(codigo);

        // THEN
        assertEquals(codigo, brand.getCodigoMarca());
    }

    @Test
    void givenBrandWhenSetNombreThenGetReturnsCorrectValue() {
        // GIVEN
        BrandModel brand = new BrandModel();
        String nombre = "Test Brand";

        // WHEN
        brand.setNombre(nombre);

        // THEN
        assertEquals(nombre, brand.getNombre());
    }

    @Test
    void givenBrandWhenSetLogoUrlThenGetReturnsCorrectValue() {
        // GIVEN
        BrandModel brand = new BrandModel();
        String logoUrl = "http://test.url/logo.png";

        // WHEN
        brand.setLogoUrl(logoUrl);

        // THEN
        assertEquals(logoUrl, brand.getLogoUrl());
    }

    @Test
    void givenBrandWhenSetUrlSiteThenGetReturnsCorrectValue() {
        // GIVEN
        BrandModel brand = new BrandModel();
        String urlSite = "https://testbrand.com";

        // WHEN
        brand.setUrlSite(urlSite);

        // THEN
        assertEquals(urlSite, brand.getUrlSite());
    }

    @Test
    void givenBrandWhenSetTimestampsThenGetReturnsCorrectValues() {
        // GIVEN
        BrandModel brand = new BrandModel();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

        // WHEN
        brand.setCreatedAt(createdAt);
        brand.setUpdatedAt(updatedAt);

        // THEN
        assertEquals(createdAt, brand.getCreatedAt());
        assertEquals(updatedAt, brand.getUpdatedAt());
    }

    @Test
    void givenBrandWhenSetPrendasThenGetReturnsCorrectValue() {
        // GIVEN
        BrandModel brand = new BrandModel();
        List<PrendaModel> prendas = givenListOfPrendas();

        // WHEN
        brand.setPrendas(prendas);

        // THEN
        assertEquals(prendas, brand.getPrendas());
        assertEquals(2, brand.getPrendas().size());
    }

    // ========== BUSINESS LOGIC TESTS ==========

    @Test
    void givenBrandWithPrendasWhenGetPrendasThenReturnsList() {
        // GIVEN
        List<PrendaModel> prendas = givenListOfPrendas();
        BrandModel brand = new BrandModel("NIKE-001", "Nike", "http://logo.png", LocalDateTime.now(),
                LocalDateTime.now(), prendas);

        // WHEN
        List<PrendaModel> result = brand.getPrendas();

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void givenBrandWithEmptyPrendasWhenGetPrendasThenReturnsEmptyList() {
        // GIVEN
        List<PrendaModel> emptyList = Collections.emptyList();
        BrandModel brand = new BrandModel("NIKE-001", "Nike", "http://logo.png", LocalDateTime.now(),
                LocalDateTime.now(), emptyList);

        // WHEN
        List<PrendaModel> result = brand.getPrendas();

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ========== HELPER METHODS (GIVEN) ==========

    private List<PrendaModel> givenListOfPrendas() {
        PrendaModel prenda1 = new PrendaModel();
        prenda1.setNombre("Shirt");
        prenda1.setTipo("superior");

        PrendaModel prenda2 = new PrendaModel();
        prenda2.setNombre("Pants");
        prenda2.setTipo("inferior");

        return Arrays.asList(prenda1, prenda2);
    }

    // ========== HELPER METHODS (THEN) ==========

    private void thenBrandHasBasicFields(BrandModel brand, String expectedCodigo, String expectedNombre,
            String expectedLogoUrl) {
        assertEquals(expectedCodigo, brand.getCodigoMarca());
        assertEquals(expectedNombre, brand.getNombre());
        assertEquals(expectedLogoUrl, brand.getLogoUrl());
    }
}
