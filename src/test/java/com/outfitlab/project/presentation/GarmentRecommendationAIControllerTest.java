package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.ConjuntoDTO;
import com.outfitlab.project.domain.useCases.garment.GetGarmentRecomendationByText;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GarmentRecommendationAIControllerTest {

    private GetGarmentRecomendationByText getGarmentRecomendationByText;
    private GarmentRecommendationAIController controller;

    @BeforeEach
    void setUp() {
        getGarmentRecomendationByText = mock(GetGarmentRecomendationByText.class);
        controller = new GarmentRecommendationAIController(getGarmentRecomendationByText);
    }

    // ========== recommendOutfit Tests ==========

    @Test
    void givenValidRequestWhenRecommendOutfitThenReturnOk() {
        GarmentRecommendationAIController.RecommendationRequest request = givenValidRecommendationRequest();
        List<ConjuntoDTO> outfits = givenOutfitsExist();

        ResponseEntity<List<ConjuntoDTO>> response = whenCallRecommendOutfit(request);

        thenResponseOkWithOutfits(response, outfits);
        thenVerifyGetGarmentRecomendationByTextCalled("outfit casual para verano", "user123");
    }

    @Test
    void givenEmptyResultsWhenRecommendOutfitThenReturnNoContent() {
        GarmentRecommendationAIController.RecommendationRequest request = givenValidRecommendationRequest();
        givenNoOutfitsFound();

        ResponseEntity<List<ConjuntoDTO>> response = whenCallRecommendOutfit(request);

        thenResponseNoContent(response);
    }

    @Test
    void givenSingleEmptyConjuntoWhenRecommendOutfitThenReturnNoContent() {
        GarmentRecommendationAIController.RecommendationRequest request = givenValidRecommendationRequest();
        givenSingleEmptyConjunto();

        ResponseEntity<List<ConjuntoDTO>> response = whenCallRecommendOutfit(request);

        thenResponseNoContent(response);
    }

    // ========== GIVEN Methods ==========

    private GarmentRecommendationAIController.RecommendationRequest givenValidRecommendationRequest() {
        GarmentRecommendationAIController.RecommendationRequest request = new GarmentRecommendationAIController.RecommendationRequest();
        request.setPeticionUsuario("outfit casual para verano");
        request.setIdUsuario("user123");
        return request;
    }

    private List<ConjuntoDTO> givenOutfitsExist() {
        ConjuntoDTO conjunto1 = mock(ConjuntoDTO.class);
        ConjuntoDTO conjunto2 = mock(ConjuntoDTO.class);

        when(conjunto1.getPrendas()).thenReturn(Arrays.asList(
                mock(PrendaModel.class),
                mock(PrendaModel.class)));
        when(conjunto2.getPrendas()).thenReturn(Arrays.asList(
                mock(PrendaModel.class),
                mock(PrendaModel.class)));

        List<ConjuntoDTO> outfits = Arrays.asList(conjunto1, conjunto2);
        when(getGarmentRecomendationByText.execute("outfit casual para verano", "user123"))
                .thenReturn(outfits);
        return outfits;
    }

    private void givenNoOutfitsFound() {
        when(getGarmentRecomendationByText.execute("outfit casual para verano", "user123"))
                .thenReturn(new ArrayList<>());
    }

    private void givenSingleEmptyConjunto() {
        ConjuntoDTO emptyConjunto = mock(ConjuntoDTO.class);
        when(emptyConjunto.getPrendas()).thenReturn(new ArrayList<>());

        when(getGarmentRecomendationByText.execute("outfit casual para verano", "user123"))
                .thenReturn(Arrays.asList(emptyConjunto));
    }

    // ========== WHEN Methods ==========

    private ResponseEntity<List<ConjuntoDTO>> whenCallRecommendOutfit(
            GarmentRecommendationAIController.RecommendationRequest request) {
        return controller.recommendOutfit(request);
    }

    // ========== THEN Methods ==========

    private void thenResponseOkWithOutfits(ResponseEntity<List<ConjuntoDTO>> response, List<ConjuntoDTO> expected) {
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
    }

    private void thenResponseNoContent(ResponseEntity<List<ConjuntoDTO>> response) {
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    private void thenVerifyGetGarmentRecomendationByTextCalled(String peticion, String userId) {
        verify(getGarmentRecomendationByText, times(1)).execute(peticion, userId);
    }
}
