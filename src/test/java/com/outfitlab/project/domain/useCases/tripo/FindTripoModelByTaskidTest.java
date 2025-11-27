package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.infrastructure.repositories.TripoRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindTripoModelByTaskidTest {

    private TripoRepository tripoRepository = mock(TripoRepositoryImpl.class);
    private FindTripoModelByTaskid findTripoModelByTaskid;
    private final String VALID_TASK_ID = "TASK-001";
    private final String INVALID_TASK_ID = "TASK-002";

    @BeforeEach
    void setUp() {
        findTripoModelByTaskid = new FindTripoModelByTaskid(tripoRepository);
    }


    @Test
    public void shouldReturnTripoModelWhenTaskIsFound() {
        TripoModel expectedModel = givenRepositoryReturnsModel(VALID_TASK_ID);

        TripoModel result = whenExecuteFindModel(VALID_TASK_ID);

        thenReturnedModelIsCorrect(result, expectedModel, VALID_TASK_ID);
    }

    @Test
    public void shouldReturnNullWhenTaskIsNotFound() {
        givenRepositoryReturnsNull(INVALID_TASK_ID);

        TripoModel result = whenExecuteFindModel(INVALID_TASK_ID);

        thenReturnedModelIsNull(result);
        thenRepositoryWasCalledOnce(INVALID_TASK_ID);
    }


    //privadoss
    private TripoModel givenRepositoryReturnsModel(String taskId) {
        TripoModel model = new TripoModel();
        model.setTaskId(taskId);

        when(tripoRepository.buscarPorTaskId(taskId)).thenReturn(model);
        return model;
    }

    private void givenRepositoryReturnsNull(String taskId) {
        when(tripoRepository.buscarPorTaskId(taskId)).thenReturn(null);
    }


    private TripoModel whenExecuteFindModel(String taskId) {
        return findTripoModelByTaskid.execute(taskId);
    }

    private void thenReturnedModelIsCorrect(TripoModel result, TripoModel expectedModel, String expectedTaskId) {
        assertNotNull(result, "El resultado no debe ser nulo.");
        assertEquals(expectedModel, result, "El modelo devuelto debe coincidir con el modelo simulado.");
        assertEquals(expectedTaskId, result.getTaskId(), "El ID de tarea debe coincidir.");
        thenRepositoryWasCalledOnce(expectedTaskId);
    }

    private void thenReturnedModelIsNull(TripoModel result) {
        assertNull(result, "El resultado debe ser nulo.");
    }

    private void thenRepositoryWasCalledOnce(String taskId) {
        verify(tripoRepository, times(1)).buscarPorTaskId(taskId);
    }
}