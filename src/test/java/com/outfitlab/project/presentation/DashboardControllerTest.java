package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.model.dto.PrendaDashboardDTO.*;
import com.outfitlab.project.domain.useCases.dashboard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardControllerTest {

    private GetTopPrendas getTopPrendas;
    private GetActividadPorDias getActividadPorDias;
    private GetTopCombos getTopCombos;
    private GetColorConversion getColorConversion;

    private DashboardController controller;

    @BeforeEach
    void setUp() {
        getTopPrendas = mock(GetTopPrendas.class);
        getActividadPorDias = mock(GetActividadPorDias.class);
        getTopCombos = mock(GetTopCombos.class);
        getColorConversion = mock(GetColorConversion.class);

        controller = new DashboardController(
                getTopPrendas,
                getActividadPorDias,
                getTopCombos,
                getColorConversion);
    }

    // ========== getTopPrendas Tests ==========

    @Test
    void givenDefaultParamsWhenGetTopPrendasThenReturnOk() {
        List<TopPrenda> topPrendas = givenTopPrendasExist();

        ResponseEntity<List<TopPrenda>> response = whenCallGetTopPrendas(5, null);

        thenResponseOkWithTopPrendas(response, topPrendas);
        thenVerifyGetTopPrendasCalled(5, null);
    }

    @Test
    void givenBrandCodeWhenGetTopPrendasThenReturnOk() {
        List<TopPrenda> topPrendas = givenTopPrendasExistForBrand();

        ResponseEntity<List<TopPrenda>> response = whenCallGetTopPrendas(10, "nike");

        thenResponseOkWithTopPrendas(response, topPrendas);
        thenVerifyGetTopPrendasCalled(10, "nike");
    }

    // ========== getActividadPorDias Tests ==========

    @Test
    void whenGetActividadPorDiasThenReturnOk() {
        List<DiaPrueba> actividad = givenActividadExists();

        ResponseEntity<List<DiaPrueba>> response = whenCallGetActividadPorDias();

        thenResponseOkWithActividad(response, actividad);
        thenVerifyGetActividadPorDiasCalled();
    }

    // ========== getTopCombos Tests ==========

    @Test
    void givenDefaultParamsWhenGetTopCombosThenReturnOk() {
        List<ComboPopular> topCombos = givenTopCombosExist();

        ResponseEntity<List<ComboPopular>> response = whenCallGetTopCombos(5, null);

        thenResponseOkWithTopCombos(response, topCombos);
        thenVerifyGetTopCombosCalled(5, null);
    }

    @Test
    void givenBrandCodeWhenGetTopCombosThenReturnOk() {
        List<ComboPopular> topCombos = givenTopCombosExistForBrand();

        ResponseEntity<List<ComboPopular>> response = whenCallGetTopCombos(3, "adidas");

        thenResponseOkWithTopCombos(response, topCombos);
        thenVerifyGetTopCombosCalled(3, "adidas");
    }

    // ========== getColorConversion Tests ==========

    @Test
    void givenNoBrandCodeWhenGetColorConversionThenReturnOk() {
        List<ColorConversion> colorConversion = givenColorConversionExists();

        ResponseEntity<List<ColorConversion>> response = whenCallGetColorConversion(null);

        thenResponseOkWithColorConversion(response, colorConversion);
        thenVerifyGetColorConversionCalled(null);
    }

    @Test
    void givenBrandCodeWhenGetColorConversionThenReturnOk() {
        List<ColorConversion> colorConversion = givenColorConversionExistsForBrand();

        ResponseEntity<List<ColorConversion>> response = whenCallGetColorConversion("puma");

        thenResponseOkWithColorConversion(response, colorConversion);
        thenVerifyGetColorConversionCalled("puma");
    }

    // ========== GIVEN Methods ==========

    private List<TopPrenda> givenTopPrendasExist() {
        List<TopPrenda> topPrendas = Arrays.asList(
                new TopPrenda(1L, "Prenda 1", "Rojo", "url1", 100, List.of()),
                new TopPrenda(2L, "Prenda 2", "Azul", "url2", 80, List.of()));
        when(getTopPrendas.execute(5, null)).thenReturn(topPrendas);
        return topPrendas;
    }

    private List<TopPrenda> givenTopPrendasExistForBrand() {
        List<TopPrenda> topPrendas = Arrays.asList(
                new TopPrenda(1L, "Nike Shirt", "Negro", "url1", 50, List.of()));
        when(getTopPrendas.execute(10, "nike")).thenReturn(topPrendas);
        return topPrendas;
    }

    private List<DiaPrueba> givenActividadExists() {
        List<DiaPrueba> actividad = Arrays.asList(
                new DiaPrueba(1, 10),
                new DiaPrueba(2, 15));
        when(getActividadPorDias.execute()).thenReturn(actividad);
        return actividad;
    }

    private List<ComboPopular> givenTopCombosExist() {
        List<ComboPopular> topCombos = Arrays.asList(
                new ComboPopular("combo1", "combo2", "url1", "url2", 50, 10));
        when(getTopCombos.execute(5, null)).thenReturn(topCombos);
        return topCombos;
    }

    private List<ComboPopular> givenTopCombosExistForBrand() {
        List<ComboPopular> topCombos = Arrays.asList(
                new ComboPopular("combo1", "combo2", "url1", "url2", 30, 5));
        when(getTopCombos.execute(3, "adidas")).thenReturn(topCombos);
        return topCombos;
    }

    private List<ColorConversion> givenColorConversionExists() {
        List<ColorConversion> colorConversion = Arrays.asList(
                new ColorConversion("Rojo", 100, 80, 80.0),
                new ColorConversion("Azul", 50, 40, 80.0));
        when(getColorConversion.execute(null)).thenReturn(colorConversion);
        return colorConversion;
    }

    private List<ColorConversion> givenColorConversionExistsForBrand() {
        List<ColorConversion> colorConversion = Arrays.asList(
                new ColorConversion("Negro", 60, 50, 83.3));
        when(getColorConversion.execute("puma")).thenReturn(colorConversion);
        return colorConversion;
    }

    // ========== WHEN Methods ==========

    private ResponseEntity<List<TopPrenda>> whenCallGetTopPrendas(int topN, String brandCode) {
        return controller.getTopPrendas(topN, brandCode);
    }

    private ResponseEntity<List<DiaPrueba>> whenCallGetActividadPorDias() {
        return controller.getActividadPorDias();
    }

    private ResponseEntity<List<ComboPopular>> whenCallGetTopCombos(int topN, String brandCode) {
        return controller.getTopCombos(topN, brandCode);
    }

    private ResponseEntity<List<ColorConversion>> whenCallGetColorConversion(String brandCode) {
        return controller.getColorConversion(brandCode);
    }

    // ========== THEN Methods ==========

    private void thenResponseOkWithTopPrendas(ResponseEntity<List<TopPrenda>> response, List<TopPrenda> expected) {
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
    }

    private void thenResponseOkWithActividad(ResponseEntity<List<DiaPrueba>> response, List<DiaPrueba> expected) {
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
    }

    private void thenResponseOkWithTopCombos(ResponseEntity<List<ComboPopular>> response, List<ComboPopular> expected) {
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
    }

    private void thenResponseOkWithColorConversion(ResponseEntity<List<ColorConversion>> response,
            List<ColorConversion> expected) {
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
    }

    private void thenVerifyGetTopPrendasCalled(int topN, String brandCode) {
        verify(getTopPrendas, times(1)).execute(topN, brandCode);
    }

    private void thenVerifyGetActividadPorDiasCalled() {
        verify(getActividadPorDias, times(1)).execute();
    }

    private void thenVerifyGetTopCombosCalled(int topN, String brandCode) {
        verify(getTopCombos, times(1)).execute(topN, brandCode);
    }

    private void thenVerifyGetColorConversionCalled(String brandCode) {
        verify(getColorConversion, times(1)).execute(brandCode);
    }
}
