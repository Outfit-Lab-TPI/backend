package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.ErrorTripoEntityNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.infrastructure.repositories.TripoRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateTripoModelTest {

    private TripoRepository tripoRepository = mock(TripoRepositoryImpl.class);
    private UpdateTripoModel updateTripoModel;

    private TripoModel mockModel;
    private TripoModel mockUpdatedModel;

    @BeforeEach
    void setUp() {
        updateTripoModel = new UpdateTripoModel(tripoRepository);
        mockModel = new TripoModel();
        mockUpdatedModel = new TripoModel();
    }


    @Test
    public void shouldReturnUpdatedModelWhenModelIsValid() throws ErrorTripoEntityNotFoundException {
        givenRepositoryReturnsUpdatedModel(mockModel, mockUpdatedModel);

        TripoModel result = whenExecuteUpdate(mockModel);

        thenReturnedModelIsCorrect(result, mockUpdatedModel, mockModel);
    }

    @Test
    public void shouldThrowErrorTripoEntityNotFoundExceptionWhenModelIsInvalid() {
        givenRepositoryThrowsNotFoundException(mockModel);

        thenExecutionThrowsNotFoundException(mockModel);
    }


    private void givenRepositoryReturnsUpdatedModel(TripoModel model, TripoModel updatedModel) throws ErrorTripoEntityNotFoundException {
        when(tripoRepository.update(model)).thenReturn(updatedModel);
    }

    private void givenRepositoryThrowsNotFoundException(TripoModel model) {
        try {
            when(tripoRepository.update(model)).thenThrow(new ErrorTripoEntityNotFoundException("No se encontró la entidad"));
        } catch (ErrorTripoEntityNotFoundException e) {
        }
    }


    private TripoModel whenExecuteUpdate(TripoModel model) throws ErrorTripoEntityNotFoundException {
        return updateTripoModel.execute(model);
    }


    private void thenReturnedModelIsCorrect(TripoModel result, TripoModel expectedUpdatedModel, TripoModel modelUsed) {
        assertNotNull(result, "El resultado no debe ser nulo.");
        assertEquals(expectedUpdatedModel, result, "El modelo devuelto debe ser el actualizado.");
        thenRepositoryUpdateWasCalled(modelUsed, 1);
    }

    private void thenExecutionThrowsNotFoundException(TripoModel modelUsed) {
        assertThrows(ErrorTripoEntityNotFoundException.class, () -> updateTripoModel.execute(modelUsed));

        thenRepositoryUpdateWasCalled(modelUsed, 1);
    }

    private void thenRepositoryUpdateWasCalled(TripoModel model, int times) {
        verify(tripoRepository, times(times)).update(model);
    }
}