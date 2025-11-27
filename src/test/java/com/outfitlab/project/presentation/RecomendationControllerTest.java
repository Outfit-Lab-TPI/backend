package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.model.GarmentRecomendationModel;
import com.outfitlab.project.domain.useCases.garment.GetGarmentRecomendation;
import com.outfitlab.project.domain.useCases.recomendations.DeleteRecomendationByPrimaryAndSecondaryGarmentCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecomendationControllerTest {

    private GetGarmentRecomendation getGarmentRecomendation;
    private DeleteRecomendationByPrimaryAndSecondaryGarmentCode deleteRecomendationByPrimaryAndSecondaryGarmentCode;

    private RecomendationController controller;

    @BeforeEach
    void setUp() {
        getGarmentRecomendation = mock(GetGarmentRecomendation.class);
        deleteRecomendationByPrimaryAndSecondaryGarmentCode = mock(
                DeleteRecomendationByPrimaryAndSecondaryGarmentCode.class);

        controller = new RecomendationController(
                getGarmentRecomendation,
                deleteRecomendationByPrimaryAndSecondaryGarmentCode);
    }

    // ========== getRecomendations Tests ==========

    @Test
    void givenValidGarmentCodeWhenGetRecomendationsThenReturnOk() {
        String garmentCode = "nike-shirt-001";
        List<GarmentRecomendationModel> recommendations = givenRecommendationsExist(garmentCode);

        ResponseEntity<?> response = whenCallGetRecomendations(garmentCode);

        thenResponseOk(response);
        thenResponseBodyEquals(response, recommendations);
        thenVerifyGetGarmentRecomendationCalled(garmentCode);
    }

    @Test
    void givenInvalidGarmentCodeWhenGetRecomendationsThenReturn404() {
        String garmentCode = "invalid-code";
        givenRecommendationNotFound(garmentCode);

        ResponseEntity<?> response = whenCallGetRecomendations(garmentCode);

        thenResponseNotFound(response, "Recomendaciones no encontradas");
    }

    // ========== deleteRecomendation Tests ==========

    @Test
    void givenValidParamsWhenDeleteRecomendationThenReturnOk() {
        String primaryCode = "garment-001";
        String secondaryCode = "garment-002";
        String type = "complemento";
        String successMessage = givenRecommendationDeleted(primaryCode, secondaryCode, type);

        ResponseEntity<?> response = whenCallDeleteRecomendation(primaryCode, secondaryCode, type);

        thenResponseOk(response);
        thenResponseBodyEquals(response, successMessage);
        thenVerifyDeleteRecomendationCalled(primaryCode, secondaryCode, type);
    }

    @Test
    void givenInvalidParamsWhenDeleteRecomendationThenReturn404() {
        String primaryCode = "invalid-001";
        String secondaryCode = "invalid-002";
        String type = "complemento";
        givenDeleteRecommendationThrowError(primaryCode, secondaryCode, type);

        ResponseEntity<?> response = whenCallDeleteRecomendation(primaryCode, secondaryCode, type);

        thenResponseNotFound(response, "Recomendación no encontrada");
    }

    // ========== GIVEN Methods ==========

    private List<GarmentRecomendationModel> givenRecommendationsExist(String garmentCode) {
        List<GarmentRecomendationModel> recommendations = Arrays.asList(
                mock(GarmentRecomendationModel.class),
                mock(GarmentRecomendationModel.class));
        when(getGarmentRecomendation.execute(garmentCode)).thenReturn(recommendations);
        return recommendations;
    }

    private void givenRecommendationNotFound(String garmentCode) {
        when(getGarmentRecomendation.execute(garmentCode))
                .thenThrow(new RuntimeException("Recomendaciones no encontradas"));
    }

    private String givenRecommendationDeleted(String primaryCode, String secondaryCode, String type) {
        String message = "Recomendación eliminada correctamente";
        when(deleteRecomendationByPrimaryAndSecondaryGarmentCode.execute(primaryCode, secondaryCode, type))
                .thenReturn(message);
        return message;
    }

    private void givenDeleteRecommendationThrowError(String primaryCode, String secondaryCode, String type) {
        when(deleteRecomendationByPrimaryAndSecondaryGarmentCode.execute(primaryCode, secondaryCode, type))
                .thenThrow(new RuntimeException("Recomendación no encontrada"));
    }

    // ========== WHEN Methods ==========

    private ResponseEntity<?> whenCallGetRecomendations(String garmentCode) {
        return controller.getRecomendations(garmentCode);
    }

    private ResponseEntity<?> whenCallDeleteRecomendation(String primaryCode, String secondaryCode, String type) {
        return controller.deleteRecomendation(primaryCode, secondaryCode, type);
    }

    // ========== THEN Methods ==========

    private void thenResponseOk(ResponseEntity<?> response) {
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    private void thenResponseBodyEquals(ResponseEntity<?> response, Object expected) {
        assertEquals(expected, response.getBody());
    }

    private void thenResponseNotFound(ResponseEntity<?> response, String expectedMessage) {
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedMessage, response.getBody());
    }

    private void thenVerifyGetGarmentRecomendationCalled(String garmentCode) {
        verify(getGarmentRecomendation, times(1)).execute(garmentCode);
    }

    private void thenVerifyDeleteRecomendationCalled(String primaryCode, String secondaryCode, String type) {
        verify(deleteRecomendationByPrimaryAndSecondaryGarmentCode, times(1))
                .execute(primaryCode, secondaryCode, type);
    }
}
