package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;
import com.outfitlab.project.infrastructure.repositories.RecomendationRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetGarmentRecomendationTest {

    private final GarmentRecomendationRepository garmentRecomendationRepository = mock(RecomendationRepositoryImpl.class);

    private final GetGarmentRecomendation getGarmentRecomendation = new GetGarmentRecomendation(garmentRecomendationRepository);

    @Test
    public void givenValidGarmentCodeWhenRecommendationsExistThenReturnListSuccessfully()
            throws GarmentNotFoundException {

        String garmentCode = givenGarmentCode("G001");
        List<GarmentRecomendationModel> expectedList = givenRecommendationListOfSize(2);

        mockRepositoryReturning(garmentCode, expectedList);

        List<GarmentRecomendationModel> result = whenExecutingWith(garmentCode);

        thenResultNotNull(result);
        thenResultHasSize(result, 2);
        thenRepositoryCalledOnce(garmentCode);
    }

    @Test
    public void givenValidGarmentCodeWhenNoRecommendationsExistThenReturnEmptyList()
            throws GarmentNotFoundException {

        String garmentCode = givenGarmentCode("G002");

        mockRepositoryReturningEmpty(garmentCode);

        List<GarmentRecomendationModel> result = whenExecutingWith(garmentCode);

        thenResultNotNull(result);
        thenResultIsEmpty(result);
        thenRepositoryCalledOnce(garmentCode);
    }

    @Test
    public void givenInvalidGarmentCodeWhenExecuteThenThrowGarmentNotFoundException()
            throws GarmentNotFoundException {

        String garmentCode = givenGarmentCode("INVALID");

        mockRepositoryThrowingNotFound(garmentCode);

        assertThrows(GarmentNotFoundException.class, () ->
                whenExecutingWith(garmentCode)
        );

        thenRepositoryCalledOnce(garmentCode);
    }

    // privadosss
    private String givenGarmentCode(String code) {
        return code;
    }

    private List<GarmentRecomendationModel> givenRecommendationListOfSize(int size) {
        return java.util.stream.Stream
                .generate(GarmentRecomendationModel::new)
                .limit(size)
                .toList();
    }

    private void mockRepositoryReturning(String garmentCode, List<GarmentRecomendationModel> result) {
        when(garmentRecomendationRepository.findRecomendationsByGarmentCode(garmentCode))
                .thenReturn(result);
    }

    private void mockRepositoryReturningEmpty(String garmentCode) {
        when(garmentRecomendationRepository.findRecomendationsByGarmentCode(garmentCode))
                .thenReturn(List.of());
    }

    private void mockRepositoryThrowingNotFound(String garmentCode) {
        when(garmentRecomendationRepository.findRecomendationsByGarmentCode(garmentCode))
                .thenThrow(new GarmentNotFoundException("Prenda no encontrada"));
    }

    private List<GarmentRecomendationModel> whenExecutingWith(String garmentCode)
            throws GarmentNotFoundException {

        return getGarmentRecomendation.execute(garmentCode);
    }

    private void thenResultNotNull(Object result) {
        assertNotNull(result);
    }

    private void thenResultHasSize(List<?> list, int size) {
        assertEquals(size, list.size());
    }

    private void thenResultIsEmpty(List<?> list) {
        assertTrue(list.isEmpty());
    }

    private void thenRepositoryCalledOnce(String garmentCode) {
        verify(garmentRecomendationRepository, times(1))
                .findRecomendationsByGarmentCode(garmentCode);
    }
}
