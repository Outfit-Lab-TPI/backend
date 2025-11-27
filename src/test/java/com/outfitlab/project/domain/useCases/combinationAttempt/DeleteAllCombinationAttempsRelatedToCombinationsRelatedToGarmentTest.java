package com.outfitlab.project.domain.useCases.combinationAttempt;

import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteAllCombinationAttempsRelatedToCombinationsRelatedToGarmentTest {

    private CombinationAttemptRepository combinationAttemptRepository = mock(CombinationAttemptRepository.class);
    private DeleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment deleteAllAttemps;

    private final String VALID_GARMENT_CODE = "PANTALON-007";
    private final String EMPTY_GARMENT_CODE = "";
    private final String NULL_GARMENT_CODE = null;

    @BeforeEach
    void setUp() {
        deleteAllAttemps = new DeleteAllCombinationAttempsRelatedToCombinationsRelatedToGarment(combinationAttemptRepository);
    }


    @Test
    public void shouldCallRepositoryToDeleteAllAttempsByValidGarmentCode() {
        whenExecuteDeleteAll(VALID_GARMENT_CODE);

        thenRepositoryDeleteAllWasCalled(VALID_GARMENT_CODE, 1);
    }

    @Test
    public void shouldCallRepositoryWithEmptyStringWhenGarmentCodeIsEmpty() {
        whenExecuteDeleteAll(EMPTY_GARMENT_CODE);

        thenRepositoryDeleteAllWasCalled(EMPTY_GARMENT_CODE, 1);
    }

    @Test
    public void shouldCallRepositoryWithNullWhenGarmentCodeIsNull() {
        whenExecuteDeleteAll(NULL_GARMENT_CODE);

        thenRepositoryDeleteAllWasCalled(NULL_GARMENT_CODE, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException(VALID_GARMENT_CODE);

        assertThrows(RuntimeException.class, () -> whenExecuteDeleteAll(VALID_GARMENT_CODE));

        thenRepositoryDeleteAllWasCalled(VALID_GARMENT_CODE, 1);
    }


    //privadoss
    private void givenRepositoryThrowsRuntimeException(String garmentCode) {
        doThrow(new RuntimeException("Simulated cascaded delete error"))
                .when(combinationAttemptRepository)
                .deleteAllByAttempsReltedToCombinationRelatedToGarments(garmentCode);
    }

    private void whenExecuteDeleteAll(String garmentCode) {
        deleteAllAttemps.execute(garmentCode);
    }

    private void thenRepositoryDeleteAllWasCalled(String expectedGarmentCode, int times) {
        verify(combinationAttemptRepository, times(times))
                .deleteAllByAttempsReltedToCombinationRelatedToGarments(expectedGarmentCode);
    }
}