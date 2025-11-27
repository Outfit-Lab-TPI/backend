package com.outfitlab.project.domain.useCases.combination;

import com.outfitlab.project.domain.interfaces.repositories.CombinationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DeleteAllCombinationRelatedToGarmentTest {

    private CombinationRepository combinationRepository = mock(CombinationRepository.class);
    private DeleteAllCombinationRelatedToGarment deleteAllCombinationRelatedToGarment;

    private final String VALID_GARMENT_CODE = "PANTALON-001";
    private final String EMPTY_GARMENT_CODE = "";
    private final String NULL_GARMENT_CODE = null;

    @BeforeEach
    void setUp() {
        deleteAllCombinationRelatedToGarment = new DeleteAllCombinationRelatedToGarment(combinationRepository);
    }


    @Test
    public void shouldCallRepositoryToDeleteAllCombinationsByValidGarmentCode() {
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
    public void shouldNotThrowExceptionIfRepositoryThrowsRuntimeException() {
        givenRepositoryThrowsRuntimeException();

        assertThrows(RuntimeException.class, () -> whenExecuteDeleteAll(VALID_GARMENT_CODE));

        thenRepositoryDeleteAllWasCalled(VALID_GARMENT_CODE, 1);
    }


    //privadoss
    private void givenRepositoryThrowsRuntimeException() {
        doThrow(new RuntimeException("Simulated DB error")).when(combinationRepository).deleteAllByGarmentcode(anyString());
    }

    private void whenExecuteDeleteAll(String garmentCode) {
        deleteAllCombinationRelatedToGarment.execute(garmentCode);
    }

    private void thenRepositoryDeleteAllWasCalled(String expectedGarmentCode, int times) {
        verify(combinationRepository, times(times)).deleteAllByGarmentcode(expectedGarmentCode);
    }
}