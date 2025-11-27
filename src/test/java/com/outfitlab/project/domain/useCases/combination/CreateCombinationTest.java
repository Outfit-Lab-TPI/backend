package com.outfitlab.project.domain.useCases.combination;

import com.outfitlab.project.domain.interfaces.repositories.CombinationRepository;
import com.outfitlab.project.domain.model.CombinationModel;
import com.outfitlab.project.domain.model.PrendaModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateCombinationTest {

    private CombinationRepository combinationRepository = mock(CombinationRepository.class);
    private CreateCombination createCombination;

    private PrendaModel mockPrendaSuperior;
    private PrendaModel mockPrendaInferior;
    private CombinationModel mockSavedModel;

    @BeforeEach
    void setUp() {
        mockPrendaSuperior = mock(PrendaModel.class);
        mockPrendaInferior = mock(PrendaModel.class);
        mockSavedModel = mock(CombinationModel.class);
        createCombination = new CreateCombination(combinationRepository);
    }


    @Test
    public void shouldSuccessfullyCreateAndSaveCombinationModel() {
        givenRepositorySavesModelAndReturns(mockSavedModel);

        CombinationModel result = whenExecuteCreateCombination(mockPrendaSuperior, mockPrendaInferior);

        thenResultMatchesSavedModel(result, mockSavedModel);
        thenRepositoryWasCalledWithCorrectPrendas(mockPrendaSuperior, mockPrendaInferior);
    }

    @Test
    public void shouldCreateCombinationModelWhenPrendaInferiorIsNull() {
        givenRepositorySavesModelAndReturns(mockSavedModel);
        PrendaModel nullPrendaInferior = null;

        CombinationModel result = whenExecuteCreateCombination(mockPrendaSuperior, nullPrendaInferior);

        thenResultMatchesSavedModel(result, mockSavedModel);
        thenRepositoryWasCalledWithCorrectPrendas(mockPrendaSuperior, nullPrendaInferior);
    }

    @Test
    public void shouldCreateCombinationModelWhenPrendaSuperiorIsNull() {
        givenRepositorySavesModelAndReturns(mockSavedModel);
        PrendaModel nullPrendaSuperior = null;

        CombinationModel result = whenExecuteCreateCombination(nullPrendaSuperior, mockPrendaInferior);

        thenResultMatchesSavedModel(result, mockSavedModel);
        thenRepositoryWasCalledWithCorrectPrendas(nullPrendaSuperior, mockPrendaInferior);
    }

    @Test
    public void shouldCreateCombinationModelWhenBothPrendasAreNull() {
        givenRepositorySavesModelAndReturns(mockSavedModel);

        CombinationModel result = whenExecuteCreateCombination(null, null);

        thenResultMatchesSavedModel(result, mockSavedModel);
        thenRepositoryWasCalledWithCorrectPrendas(null, null);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFailsToSave() {
        givenRepositoryThrowsRuntimeException();

        assertThrows(RuntimeException.class,
                () -> whenExecuteCreateCombination(mockPrendaSuperior, mockPrendaInferior),
                "Se esperaba que la excepción de persistencia se propagara.");

        verify(combinationRepository, times(1)).save(any(CombinationModel.class));
    }


    //privadosss
    private void givenRepositorySavesModelAndReturns(CombinationModel savedModel) {
        when(combinationRepository.save(any(CombinationModel.class))).thenReturn(savedModel);
    }

    private void givenRepositoryThrowsRuntimeException() {
        doThrow(new RuntimeException("Simulated DB error")).when(combinationRepository).save(any(CombinationModel.class));
    }

    private CombinationModel whenExecuteCreateCombination(PrendaModel superior, PrendaModel inferior) {
        return createCombination.execute(superior, inferior);
    }

    private void thenResultMatchesSavedModel(CombinationModel actualResult, CombinationModel expectedSavedModel) {
        assertNotNull(actualResult, "El resultado no debe ser nulo.");
        assertEquals(expectedSavedModel, actualResult, "El resultado debe ser el modelo devuelto por el repositorio.");
    }

    private void thenRepositoryWasCalledWithCorrectPrendas(PrendaModel expectedSuperior, PrendaModel expectedInferior) {
        ArgumentCaptor<CombinationModel> captor = ArgumentCaptor.forClass(CombinationModel.class);
        verify(combinationRepository, times(1)).save(captor.capture());

        CombinationModel capturedModel = captor.getValue();

        assertEquals(expectedSuperior, capturedModel.getPrendaSuperior(), "El modelo debe contener la prenda superior correcta.");
        assertEquals(expectedInferior, capturedModel.getPrendaInferior(), "El modelo debe contener la prenda inferior correcta.");
    }
}