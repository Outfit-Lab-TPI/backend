package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DeleteRecomendationByPrimaryAndSecondaryGarmentCodeTest {

    private GarmentRecomendationRepository garmentRecomendationRepository = mock(GarmentRecomendationRepository.class);
    private DeleteRecomendationByPrimaryAndSecondaryGarmentCode deleteRecomendationByPrimaryAndSecondaryGarmentCode;

    private final String PRIMARY_CODE = "superior-1";
    private final String SECONDARY_CODE = "inferior-2";
    private final String TYPE_CODE = "matching";
    private final String SUCCESS_MESSAGE = "Sugerencia eliminada con éxito.";
    private final String NULL_CODE = null;
    private final String EMPTY_CODE = "";

    @BeforeEach
    void setUp() {
        deleteRecomendationByPrimaryAndSecondaryGarmentCode = new DeleteRecomendationByPrimaryAndSecondaryGarmentCode(garmentRecomendationRepository);
    }


    @Test
    public void shouldCallRepositoryAndDeleteSugerenciaSuccessfully() {
        String result = whenExecuteDelete(PRIMARY_CODE, SECONDARY_CODE, TYPE_CODE);

        assertEquals(SUCCESS_MESSAGE, result, "Debe retornar el mensaje de éxito.");
        thenRepositoryDeleteWasCalled(PRIMARY_CODE, SECONDARY_CODE, TYPE_CODE, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenPrimaryCodeIsNull() {
        givenRepositoryThrowsRuntimeException(NULL_CODE, SECONDARY_CODE, TYPE_CODE);

        assertThrows(RuntimeException.class,
                () -> whenExecuteDelete(NULL_CODE, SECONDARY_CODE, TYPE_CODE),
                "Se espera que el RuntimeException se propague al delegar el código primario nulo.");

        thenRepositoryDeleteWasCalled(NULL_CODE, SECONDARY_CODE, TYPE_CODE, 1);
    }

    @Test
    public void shouldCallRepositoryWhenSecondaryCodeIsEmpty() {
        whenExecuteDelete(PRIMARY_CODE, EMPTY_CODE, TYPE_CODE);

        thenRepositoryDeleteWasCalled(PRIMARY_CODE, EMPTY_CODE, TYPE_CODE, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenTypeIsNull() {
        givenRepositoryThrowsRuntimeException(PRIMARY_CODE, SECONDARY_CODE, NULL_CODE);

        assertThrows(RuntimeException.class,
                () -> whenExecuteDelete(PRIMARY_CODE, SECONDARY_CODE, NULL_CODE),
                "Se espera que el RuntimeException se propague al delegar el tipo nulo.");

        thenRepositoryDeleteWasCalled(PRIMARY_CODE, SECONDARY_CODE, NULL_CODE, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException(PRIMARY_CODE, SECONDARY_CODE, TYPE_CODE);

        assertThrows(RuntimeException.class,
                () -> whenExecuteDelete(PRIMARY_CODE, SECONDARY_CODE, TYPE_CODE),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryDeleteWasCalled(PRIMARY_CODE, SECONDARY_CODE, TYPE_CODE, 1);
    }


    //privadossss
    private void givenRepositoryThrowsRuntimeException(String primaryCode, String secondaryCode, String type) {
        doThrow(new RuntimeException("Simulated DB error"))
                .when(garmentRecomendationRepository).deleteRecomendationByGarmentsCode(primaryCode, secondaryCode, type);
    }

    private String whenExecuteDelete(String primaryCode, String secondaryCode, String type) {
        return deleteRecomendationByPrimaryAndSecondaryGarmentCode.execute(primaryCode, secondaryCode, type);
    }

    private void thenRepositoryDeleteWasCalled(String expectedPrimary, String expectedSecondary, String expectedType, int times) {
        verify(garmentRecomendationRepository, times(times)).deleteRecomendationByGarmentsCode(expectedPrimary, expectedSecondary, expectedType);
    }
}