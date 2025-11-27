package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteGarmentRecomentationsRelatedToGarmentTest {

    private GarmentRecomendationRepository garmentRecomendationRepository = mock(GarmentRecomendationRepository.class);
    private DeleteGarmentRecomentationsRelatedToGarment deleteGarmentRecomentationsRelatedToGarment;

    private final String VALID_GARMENT_CODE = "PANTALON-001";
    private final String EMPTY_GARMENT_CODE = "";
    private final String NULL_GARMENT_CODE = null;

    @BeforeEach
    void setUp() {
        deleteGarmentRecomentationsRelatedToGarment = new DeleteGarmentRecomentationsRelatedToGarment(garmentRecomendationRepository);
    }


    @Test
    public void shouldCallRepositoryToDeleteRecomendationsByValidGarmentCode() {
        whenExecuteDelete(VALID_GARMENT_CODE);

        thenRepositoryDeleteWasCalled(VALID_GARMENT_CODE, 1);
    }

    @Test
    public void shouldCallRepositoryWhenGarmentCodeIsEmpty() {
        whenExecuteDelete(EMPTY_GARMENT_CODE);

        thenRepositoryDeleteWasCalled(EMPTY_GARMENT_CODE, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenGarmentCodeIsNull() {
        givenRepositoryThrowsRuntimeException(NULL_GARMENT_CODE);

        assertThrows(RuntimeException.class,
                () -> whenExecuteDelete(NULL_GARMENT_CODE),
                "Se espera que el RuntimeException del repositorio/framework se propague al delegar null.");

        thenRepositoryDeleteWasCalled(NULL_GARMENT_CODE, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException(VALID_GARMENT_CODE);

        assertThrows(RuntimeException.class,
                () -> whenExecuteDelete(VALID_GARMENT_CODE),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryDeleteWasCalled(VALID_GARMENT_CODE, 1);
    }


    //privadoss
    private void givenRepositoryThrowsRuntimeException(String garmentCode) {
        doThrow(new RuntimeException("Simulated DB error"))
                .when(garmentRecomendationRepository).deleteRecomendationsByGarmentCode(garmentCode);
    }

    private void whenExecuteDelete(String garmentCode) {
        deleteGarmentRecomentationsRelatedToGarment.execute(garmentCode);
    }

    private void thenRepositoryDeleteWasCalled(String expectedGarmentCode, int times) {
        verify(garmentRecomendationRepository, times(times)).deleteRecomendationsByGarmentCode(expectedGarmentCode);
    }
}