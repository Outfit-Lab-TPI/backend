package com.outfitlab.project.domain.useCases.combination;

import com.outfitlab.project.domain.exceptions.CombinationNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.CombinationRepository;
import com.outfitlab.project.domain.model.CombinationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetCombinationByPrendasTest {

    private CombinationRepository combinationRepository = mock(CombinationRepository.class);
    private GetCombinationByPrendas getCombinationByPrendas;

    private final Long SUPERIOR_ID = 101L;
    private final Long INFERIOR_ID = 202L;
    private final Long NULL_ID = null;

    private CombinationModel mockCombination;

    @BeforeEach
    void setUp() {
        mockCombination = mock(CombinationModel.class);
        getCombinationByPrendas = new GetCombinationByPrendas(combinationRepository);
    }


    @Test
    public void shouldReturnCombinationWhenBothPrendaIdsAreFound() throws CombinationNotFoundException {
        givenRepositoryFindsCombination(SUPERIOR_ID, INFERIOR_ID, mockCombination);

        CombinationModel result = whenExecuteGetCombination(SUPERIOR_ID, INFERIOR_ID);

        thenResultMatchesExpectedCombination(result, mockCombination);
        thenRepositoryWasCalledOnce(SUPERIOR_ID, INFERIOR_ID);
    }

    @Test
    public void shouldThrowCombinationNotFoundExceptionWhenCombinationDoesNotExist() {
        givenRepositoryFindsNoCombination(SUPERIOR_ID, INFERIOR_ID);

        assertThrows(CombinationNotFoundException.class,
                () -> whenExecuteGetCombination(SUPERIOR_ID, INFERIOR_ID),
                "Debe lanzar CombinationNotFoundException cuando Optional está vacío.");

        thenRepositoryWasCalledOnce(SUPERIOR_ID, INFERIOR_ID);
    }

    @Test
    public void shouldThrowCombinationNotFoundExceptionWhenSuperiorIdIsNullAndCombinationDoesNotExist() {
        givenRepositoryFindsNoCombination(NULL_ID, INFERIOR_ID);

        assertThrows(CombinationNotFoundException.class,
                () -> whenExecuteGetCombination(NULL_ID, INFERIOR_ID),
                "Debe fallar si no se encuentra combinación con ID Superior nulo.");

        thenRepositoryWasCalledOnce(NULL_ID, INFERIOR_ID);
    }

    @Test
    public void shouldThrowCombinationNotFoundExceptionWhenInferiorIdIsNullAndCombinationDoesNotExist() {
        givenRepositoryFindsNoCombination(SUPERIOR_ID, NULL_ID);

        assertThrows(CombinationNotFoundException.class,
                () -> whenExecuteGetCombination(SUPERIOR_ID, NULL_ID),
                "Debe fallar si no se encuentra combinación con ID Inferior nulo.");

        thenRepositoryWasCalledOnce(SUPERIOR_ID, NULL_ID);
    }

    @Test
    public void shouldThrowCombinationNotFoundExceptionWhenBothIdsAreNullAndCombinationDoesNotExist() {
        givenRepositoryFindsNoCombination(NULL_ID, NULL_ID);

        assertThrows(CombinationNotFoundException.class,
                () -> whenExecuteGetCombination(NULL_ID, NULL_ID),
                "Debe fallar si no se encuentra combinación con ambos IDs nulos.");

        thenRepositoryWasCalledOnce(NULL_ID, NULL_ID);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException(SUPERIOR_ID, INFERIOR_ID);

        assertThrows(RuntimeException.class,
                () -> whenExecuteGetCombination(SUPERIOR_ID, INFERIOR_ID),
                "La excepción de tiempo de ejecución debe ser propagada.");

        thenRepositoryWasCalledOnce(SUPERIOR_ID, INFERIOR_ID);
    }


    //privadosss
    private void givenRepositoryFindsCombination(Long superiorId, Long inferiorId, CombinationModel model) {
        when(combinationRepository.findByPrendas(superiorId, inferiorId)).thenReturn(Optional.of(model));
    }

    private void givenRepositoryFindsNoCombination(Long superiorId, Long inferiorId) {
        when(combinationRepository.findByPrendas(superiorId, inferiorId)).thenReturn(Optional.empty());
    }

    private void givenRepositoryThrowsRuntimeException(Long superiorId, Long inferiorId) {
        doThrow(new RuntimeException("Simulated DB error")).when(combinationRepository).findByPrendas(superiorId, inferiorId);
    }


    private CombinationModel whenExecuteGetCombination(Long superiorId, Long inferiorId) throws CombinationNotFoundException {
        return getCombinationByPrendas.execute(superiorId, inferiorId);
    }

    private void thenResultMatchesExpectedCombination(CombinationModel actual, CombinationModel expected) {
        assertNotNull(actual, "El resultado no debe ser nulo.");
        assertEquals(expected, actual, "La combinación devuelta debe coincidir con la simulada.");
    }

    private void thenRepositoryWasCalledOnce(Long superiorId, Long inferiorId) {
        verify(combinationRepository, times(1)).findByPrendas(superiorId, inferiorId);
    }
}