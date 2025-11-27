package com.outfitlab.project.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CombinationModelTest {

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void givenEmptyConstructorWhenCreateThenFieldsAreNull() {
        // WHEN
        CombinationModel combination = new CombinationModel();

        // THEN
        assertNull(combination.getId());
        assertNull(combination.getPrendaSuperior());
        assertNull(combination.getPrendaInferior());
    }

    @Test
    void givenPrendasWhenCreateWith2ParamsThenFieldsAreSet() {
        // GIVEN
        PrendaModel top = givenTopGarment();
        PrendaModel bottom = givenBottomGarment();

        // WHEN
        CombinationModel combination = new CombinationModel(top, bottom);

        // THEN
        assertEquals(top, combination.getPrendaSuperior());
        assertEquals(bottom, combination.getPrendaInferior());
        assertNull(combination.getId());
    }

    // ========== SETTER/GETTER TESTS ==========

    @Test
    void givenCombinationWhenSetIdThenGetReturnsCorrectValue() {
        // GIVEN
        CombinationModel combination = new CombinationModel();
        Long id = 100L;

        // WHEN
        combination.setId(id);

        // THEN
        assertEquals(id, combination.getId());
    }

    @Test
    void givenCombinationWhenSetPrendaSuperiorThenGetReturnsCorrectValue() {
        // GIVEN
        CombinationModel combination = new CombinationModel();
        PrendaModel top = givenTopGarment();

        // WHEN
        combination.setPrendaSuperior(top);

        // THEN
        assertEquals(top, combination.getPrendaSuperior());
        assertEquals("Shirt", combination.getPrendaSuperior().getNombre());
    }

    @Test
    void givenCombinationWhenSetPrendaInferiorThenGetReturnsCorrectValue() {
        // GIVEN
        CombinationModel combination = new CombinationModel();
        PrendaModel bottom = givenBottomGarment();

        // WHEN
        combination.setPrendaInferior(bottom);

        // THEN
        assertEquals(bottom, combination.getPrendaInferior());
        assertEquals("Pants", combination.getPrendaInferior().getNombre());
    }

    // ========== BUSINESS LOGIC TESTS ==========

    @Test
    void givenValidCombinationWhenCheckPrendasThenBothArePresent() {
        // GIVEN
        PrendaModel top = givenTopGarment();
        PrendaModel bottom = givenBottomGarment();

        // WHEN
        CombinationModel combination = new CombinationModel(top, bottom);

        // THEN
        assertNotNull(combination.getPrendaSuperior());
        assertNotNull(combination.getPrendaInferior());
        assertEquals("superior", combination.getPrendaSuperior().getTipo());
        assertEquals("inferior", combination.getPrendaInferior().getTipo());
    }

    @Test
    void givenCombinationWithNullTopWhenCheckThenTopIsNull() {
        // GIVEN
        PrendaModel bottom = givenBottomGarment();

        // WHEN
        CombinationModel combination = new CombinationModel(null, bottom);

        // THEN
        assertNull(combination.getPrendaSuperior());
        assertNotNull(combination.getPrendaInferior());
    }

    @Test
    void givenCombinationWithNullBottomWhenCheckThenBottomIsNull() {
        // GIVEN
        PrendaModel top = givenTopGarment();

        // WHEN
        CombinationModel combination = new CombinationModel(top, null);

        // THEN
        assertNotNull(combination.getPrendaSuperior());
        assertNull(combination.getPrendaInferior());
    }

    // ========== HELPER METHODS (GIVEN) ==========

    private PrendaModel givenTopGarment() {
        PrendaModel prenda = new PrendaModel();
        prenda.setNombre("Shirt");
        prenda.setTipo("superior");
        prenda.setGarmentCode("SHIRT-001");
        return prenda;
    }

    private PrendaModel givenBottomGarment() {
        PrendaModel prenda = new PrendaModel();
        prenda.setNombre("Pants");
        prenda.setTipo("inferior");
        prenda.setGarmentCode("PANTS-001");
        return prenda;
    }
}
