package com.outfitlab.project.domain.model.dto;

import com.outfitlab.project.domain.model.PrendaModel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConjuntoDTOTest {

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void givenValidDataWhenCreateWithConstructorThenAllFieldsAreSet() {
        // GIVEN
        String nombre = "Outfit Casual";
        List<PrendaModel> prendas = givenListOfPrendas();

        // WHEN
        ConjuntoDTO dto = new ConjuntoDTO(nombre, prendas);

        // THEN
        thenDtoHasCorrectFields(dto, nombre, prendas);
    }

    @Test
    void givenEmptyConstructorWhenCreateThenFieldsAreNull() {
        // WHEN
        ConjuntoDTO dto = new ConjuntoDTO();

        // THEN
        assertNull(dto.getNombre());
        assertNull(dto.getPrendas());
    }

    // ========== SETTER/GETTER TESTS ==========

    @Test
    void givenDtoWhenSetNombreThenGetReturnsCorrectValue() {
        // GIVEN
        ConjuntoDTO dto = new ConjuntoDTO();
        String nombre = "Outfit Formal";

        // WHEN
        dto.setNombre(nombre);

        // THEN
        assertEquals(nombre, dto.getNombre());
    }

    @Test
    void givenDtoWhenSetPrendasThenGetReturnsCorrectValue() {
        // GIVEN
        ConjuntoDTO dto = new ConjuntoDTO();
        List<PrendaModel> prendas = givenListOfPrendas();

        // WHEN
        dto.setPrendas(prendas);

        // THEN
        assertEquals(prendas, dto.getPrendas());
        assertEquals(2, dto.getPrendas().size());
    }

    // ========== BUSINESS LOGIC TESTS ==========

    @Test
    void givenConjuntoWithMultiplePrendasWhenGetPrendasThenReturnsList() {
        // GIVEN
        List<PrendaModel> prendas = givenListOfPrendas();
        ConjuntoDTO dto = new ConjuntoDTO("Outfit Deportivo", prendas);

        // WHEN
        List<PrendaModel> result = dto.getPrendas();

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void givenConjuntoWithEmptyPrendasWhenGetPrendasThenReturnsEmptyList() {
        // GIVEN
        List<PrendaModel> emptyList = Collections.emptyList();
        ConjuntoDTO dto = new ConjuntoDTO("Outfit Vacío", emptyList);

        // WHEN
        List<PrendaModel> result = dto.getPrendas();

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void givenConjuntoWithNullPrendasWhenGetPrendasThenReturnsNull() {
        // GIVEN
        ConjuntoDTO dto = new ConjuntoDTO("Outfit Sin Prendas", null);

        // WHEN
        List<PrendaModel> result = dto.getPrendas();

        // THEN
        assertNull(result);
    }

    // ========== HELPER METHODS (GIVEN) ==========

    private List<PrendaModel> givenListOfPrendas() {
        PrendaModel prenda1 = new PrendaModel();
        prenda1.setNombre("Camisa");
        prenda1.setTipo("superior");

        PrendaModel prenda2 = new PrendaModel();
        prenda2.setNombre("Pantalón");
        prenda2.setTipo("inferior");

        return Arrays.asList(prenda1, prenda2);
    }

    // ========== HELPER METHODS (THEN) ==========

    private void thenDtoHasCorrectFields(ConjuntoDTO dto, String expectedNombre, List<PrendaModel> expectedPrendas) {
        assertEquals(expectedNombre, dto.getNombre());
        assertEquals(expectedPrendas, dto.getPrendas());
    }
}
