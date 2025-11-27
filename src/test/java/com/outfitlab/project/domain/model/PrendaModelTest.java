package com.outfitlab.project.domain.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PrendaModelTest {

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void givenEmptyConstructorWhenCreateThenFieldsAreNull() {
        // WHEN
        PrendaModel prenda = new PrendaModel();

        // THEN
        assertNull(prenda.getId());
        assertNull(prenda.getNombre());
        assertNull(prenda.getMarca());
        assertNull(prenda.getTipo());
        assertNull(prenda.getImagenUrl());
        assertNull(prenda.getGarmentCode());
        assertNull(prenda.getColor());
        assertNull(prenda.getClimaAdecuado());
        assertNull(prenda.getOcasiones());
        assertNull(prenda.getGenero());
    }

    @Test
    void givenBasicDataWhenCreateWith4ParamsThenFieldsAreSet() {
        // GIVEN
        Long id = 1L;
        String nombre = "Shirt";
        String imagenUrl = "http://img.url/shirt.jpg";
        BrandModel marca = givenBrandModel();

        // WHEN
        PrendaModel prenda = new PrendaModel(id, nombre, imagenUrl, marca);

        // THEN
        assertEquals(id, prenda.getId());
        assertEquals(nombre, prenda.getNombre());
        assertEquals(imagenUrl, prenda.getImagenUrl());
        assertEquals(marca, prenda.getMarca());
    }

    @Test
    void givenFullDataWhenCreateWith8ParamsThenAllFieldsAreSet() {
        // GIVEN
        String nombre = "Pants";
        BrandModel marca = givenBrandModel();
        String tipo = "inferior";
        String imagenUrl = "http://img.url/pants.jpg";
        String garmentCode = "PANTS-001";
        ColorModel color = givenColorModel();
        ClimaModel clima = givenClimaModel();
        Set<OcasionModel> ocasiones = givenOcasiones();

        // WHEN
        PrendaModel prenda = new PrendaModel(nombre, marca, tipo, imagenUrl, garmentCode, color, clima, ocasiones);

        // THEN
        thenPrendaHasBasicFields(prenda, nombre, marca, tipo, imagenUrl, garmentCode);
        assertEquals(color, prenda.getColor());
        assertEquals(clima, prenda.getClimaAdecuado());
        assertEquals(ocasiones, prenda.getOcasiones());
        assertNull(prenda.getGenero()); // Not set in 8-param constructor
    }

    @Test
    void givenFullDataWithGeneroWhenCreateWith9ParamsThenAllFieldsAreSet() {
        // GIVEN
        String nombre = "Jacket";
        BrandModel marca = givenBrandModel();
        String tipo = "superior";
        String imagenUrl = "http://img.url/jacket.jpg";
        String garmentCode = "JACKET-001";
        ColorModel color = givenColorModel();
        ClimaModel clima = givenClimaModel();
        Set<OcasionModel> ocasiones = givenOcasiones();
        String genero = "Masculino";

        // WHEN
        PrendaModel prenda = new PrendaModel(nombre, marca, tipo, imagenUrl, garmentCode, color, clima, ocasiones,
                genero);

        // THEN
        thenPrendaHasBasicFields(prenda, nombre, marca, tipo, imagenUrl, garmentCode);
        assertEquals(color, prenda.getColor());
        assertEquals(clima, prenda.getClimaAdecuado());
        assertEquals(ocasiones, prenda.getOcasiones());
        assertEquals(genero, prenda.getGenero());
    }

    // ========== SETTER/GETTER TESTS ==========

    @Test
    void givenPrendaWhenSetIdThenGetReturnsCorrectValue() {
        // GIVEN
        PrendaModel prenda = new PrendaModel();
        Long id = 100L;

        // WHEN
        prenda.setId(id);

        // THEN
        assertEquals(id, prenda.getId());
    }

    @Test
    void givenPrendaWhenSetNombreThenGetReturnsCorrectValue() {
        // GIVEN
        PrendaModel prenda = new PrendaModel();
        String nombre = "T-Shirt";

        // WHEN
        prenda.setNombre(nombre);

        // THEN
        assertEquals(nombre, prenda.getNombre());
    }

    @Test
    void givenPrendaWhenSetMarcaThenGetReturnsCorrectValue() {
        // GIVEN
        PrendaModel prenda = new PrendaModel();
        BrandModel marca = givenBrandModel();

        // WHEN
        prenda.setMarca(marca);

        // THEN
        assertEquals(marca, prenda.getMarca());
        assertEquals("Nike", prenda.getMarca().getNombre());
    }

    @Test
    void givenPrendaWhenSetTipoThenGetReturnsCorrectValue() {
        // GIVEN
        PrendaModel prenda = new PrendaModel();
        String tipo = "calzado";

        // WHEN
        prenda.setTipo(tipo);

        // THEN
        assertEquals(tipo, prenda.getTipo());
    }

    @Test
    void givenPrendaWhenSetColorThenGetReturnsCorrectValue() {
        // GIVEN
        PrendaModel prenda = new PrendaModel();
        ColorModel color = givenColorModel();

        // WHEN
        prenda.setColor(color);

        // THEN
        assertEquals(color, prenda.getColor());
        assertEquals("Rojo", prenda.getColor().getNombre());
    }

    @Test
    void givenPrendaWhenSetClimaThenGetReturnsCorrectValue() {
        // GIVEN
        PrendaModel prenda = new PrendaModel();
        ClimaModel clima = givenClimaModel();

        // WHEN
        prenda.setClimaAdecuado(clima);

        // THEN
        assertEquals(clima, prenda.getClimaAdecuado());
        assertEquals("Calido", prenda.getClimaAdecuado().getNombre());
    }

    @Test
    void givenPrendaWhenSetOcasionesThenGetReturnsCorrectValue() {
        // GIVEN
        PrendaModel prenda = new PrendaModel();
        Set<OcasionModel> ocasiones = givenOcasiones();

        // WHEN
        prenda.setOcasiones(ocasiones);

        // THEN
        assertEquals(ocasiones, prenda.getOcasiones());
        assertEquals(2, prenda.getOcasiones().size());
    }

    @Test
    void givenPrendaWhenSetGeneroThenGetReturnsCorrectValue() {
        // GIVEN
        PrendaModel prenda = new PrendaModel();
        String genero = "Femenino";

        // WHEN
        prenda.setGenero(genero);

        // THEN
        assertEquals(genero, prenda.getGenero());
    }

    // ========== BUSINESS LOGIC TESTS ==========

    @Test
    void givenTopGarmentWhenCheckTipoThenIsSuperior() {
        // GIVEN
        PrendaModel prenda = new PrendaModel();
        prenda.setTipo("superior");

        // WHEN & THEN
        assertEquals("superior", prenda.getTipo());
    }

    @Test
    void givenBottomGarmentWhenCheckTipoThenIsInferior() {
        // GIVEN
        PrendaModel prenda = new PrendaModel();
        prenda.setTipo("inferior");

        // WHEN & THEN
        assertEquals("inferior", prenda.getTipo());
    }

    @Test
    void givenPrendaWithMultipleOcasionesWhenGetOcasionesThenReturnsSet() {
        // GIVEN
        Set<OcasionModel> ocasiones = givenOcasiones();
        PrendaModel prenda = new PrendaModel();
        prenda.setOcasiones(ocasiones);

        // WHEN
        Set<OcasionModel> result = prenda.getOcasiones();

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ========== HELPER METHODS (GIVEN) ==========

    private BrandModel givenBrandModel() {
        return new BrandModel("NIKE-001", "Nike", "http://logo.url/nike.png");
    }

    private ColorModel givenColorModel() {
        ColorModel color = new ColorModel();
        color.setNombre("Rojo");
        return color;
    }

    private ClimaModel givenClimaModel() {
        ClimaModel clima = new ClimaModel();
        clima.setNombre("Calido");
        return clima;
    }

    private Set<OcasionModel> givenOcasiones() {
        OcasionModel casual = new OcasionModel();
        casual.setNombre("Casual");

        OcasionModel formal = new OcasionModel();
        formal.setNombre("Formal");

        Set<OcasionModel> ocasiones = new HashSet<>();
        ocasiones.add(casual);
        ocasiones.add(formal);
        return ocasiones;
    }

    // ========== HELPER METHODS (THEN) ==========

    private void thenPrendaHasBasicFields(PrendaModel prenda, String expectedNombre, BrandModel expectedMarca,
            String expectedTipo, String expectedImagenUrl, String expectedGarmentCode) {
        assertEquals(expectedNombre, prenda.getNombre());
        assertEquals(expectedMarca, prenda.getMarca());
        assertEquals(expectedTipo, prenda.getTipo());
        assertEquals(expectedImagenUrl, prenda.getImagenUrl());
        assertEquals(expectedGarmentCode, prenda.getGarmentCode());
    }
}
