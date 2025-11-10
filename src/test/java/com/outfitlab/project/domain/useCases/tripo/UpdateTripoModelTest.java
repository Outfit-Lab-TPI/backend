package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.exceptions.ErrorTripoEntityNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.infrastructure.repositories.TripoRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateTripoModelTest {

    private TripoRepository tripoRepository = mock(TripoRepositoryImpl.class);
    private UpdateTripoModel updateTripoModel = new UpdateTripoModel(tripoRepository);

    @Test
    public void givenValidModelWhenExecuteThenReturnUpdatedModel() throws ErrorTripoEntityNotFoundException {
        TripoModel model = new TripoModel();
        TripoModel updatedModel = new TripoModel();

        when(tripoRepository.update(model)).thenReturn(updatedModel);

        TripoModel result = updateTripoModel.execute(model);

        assertNotNull(result);
        assertEquals(updatedModel, result);
        verify(tripoRepository, times(1)).update(model);
    }

    @Test
    public void givenInvalidTripoEntityWhenUpdateTripoModelThenThrowErrorTripoEntityNotFoundException() throws ErrorTripoEntityNotFoundException {
        TripoModel model = new TripoModel();

        when(tripoRepository.update(model)).thenThrow(new ErrorTripoEntityNotFoundException("No se encontró la entidad"));

        assertThrows(ErrorTripoEntityNotFoundException.class, () -> updateTripoModel.execute(model));
        verify(tripoRepository, times(1)).update(model);
    }
}

