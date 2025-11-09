package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;
import com.outfitlab.project.infrastructure.repositories.RecomendationRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetGarmentRecomendationTest {

    private GarmentRecomendationRepository garmentRecomendationRepository = mock(RecomendationRepository.class);
    private GetGarmentRecomendation getGarmentRecomendation = new GetGarmentRecomendation(garmentRecomendationRepository);

    @Test
    void givenValidGarmentCodeWhenRecommendationsExistThenReturnListSuccessfully() throws GarmentNotFoundException {
        String garmentCode = "G001";
        List<GarmentRecomendationModel> recomendations = List.of(new GarmentRecomendationModel(), new GarmentRecomendationModel());

        when(garmentRecomendationRepository.findRecomendationsByGarmentCode(garmentCode))
                .thenReturn(recomendations);

        List<GarmentRecomendationModel> result = getGarmentRecomendation.execute(garmentCode);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(garmentRecomendationRepository, times(1)).findRecomendationsByGarmentCode(garmentCode);
    }

    @Test
    void givenValidGarmentCodeWhenNoRecommendationsExistThenReturnEmptyList() throws GarmentNotFoundException {
        String garmentCode = "G002";

        when(garmentRecomendationRepository.findRecomendationsByGarmentCode(garmentCode))
                .thenReturn(List.of());

        List<GarmentRecomendationModel> result = getGarmentRecomendation.execute(garmentCode);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(garmentRecomendationRepository, times(1)).findRecomendationsByGarmentCode(garmentCode);
    }

    @Test
    void givenInvalidGarmentCodeWhenExecuteThenThrowGarmentNotFoundException() throws GarmentNotFoundException {
        String garmentCode = "cualquiera";

        when(garmentRecomendationRepository.findRecomendationsByGarmentCode(garmentCode))
                .thenThrow(new GarmentNotFoundException("Prenda no encontrada"));

        assertThrows(GarmentNotFoundException.class, () ->
                getGarmentRecomendation.execute(garmentCode)
        );

        verify(garmentRecomendationRepository, times(1)).findRecomendationsByGarmentCode(garmentCode);
    }
}
