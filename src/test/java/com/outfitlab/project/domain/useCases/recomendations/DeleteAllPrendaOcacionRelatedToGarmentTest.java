package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.PrendaOcasionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteAllPrendaOcacionRelatedToGarmentTest {

    private PrendaOcasionRepository prendaOcasionRepository = mock(PrendaOcasionRepository.class);
    private DeleteAllPrendaOcacionRelatedToGarment deleteAllPrendaOcacionRelatedToGarment;

    private final String VALID_GARMENT_CODE = "REMERA-001";
    private final String EMPTY_GARMENT_CODE = "";
    private final String NULL_GARMENT_CODE = null;

    @BeforeEach
    void setUp() {
        deleteAllPrendaOcacionRelatedToGarment = new DeleteAllPrendaOcacionRelatedToGarment(prendaOcasionRepository);
    }

    @Test
    public void shouldCallRepositoryToDeleteAllPrendaOcacionByValidGarmentCode() {
        whenExecuteDeleteAll(VALID_GARMENT_CODE);

        thenRepositoryDeleteAllWasCalled(VALID_GARMENT_CODE, 1);
    }

    @Test
    public void shouldCallRepositoryWhenGarmentCodeIsEmpty() {
        whenExecuteDeleteAll(EMPTY_GARMENT_CODE);

        thenRepositoryDeleteAllWasCalled(EMPTY_GARMENT_CODE, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenGarmentCodeIsNull() {
        givenRepositoryThrowsRuntimeException(NULL_GARMENT_CODE);

        assertThrows(RuntimeException.class,
                () -> whenExecuteDeleteAll(NULL_GARMENT_CODE),
                "Se espera que el RuntimeException del repositorio/framework se propague al delegar null.");

        thenRepositoryDeleteAllWasCalled(NULL_GARMENT_CODE, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException(VALID_GARMENT_CODE);

        assertThrows(RuntimeException.class,
                () -> whenExecuteDeleteAll(VALID_GARMENT_CODE),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryDeleteAllWasCalled(VALID_GARMENT_CODE, 1);
    }

    // privadoss
    private void givenRepositoryThrowsRuntimeException(String garmentCode) {
        doThrow(new RuntimeException("Simulated DB error"))
                .when(prendaOcasionRepository).deleteAllPrendaOcasionByGarment(garmentCode);
    }

    private void whenExecuteDeleteAll(String garmentCode) {
        deleteAllPrendaOcacionRelatedToGarment.execute(garmentCode);
    }

    private void thenRepositoryDeleteAllWasCalled(String expectedGarmentCode, int times) {
        verify(prendaOcasionRepository, times(times)).deleteAllPrendaOcasionByGarment(expectedGarmentCode);
    }
}