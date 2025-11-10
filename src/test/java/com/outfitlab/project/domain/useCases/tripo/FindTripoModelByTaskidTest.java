package com.outfitlab.project.domain.useCases.tripo;

import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.infrastructure.repositories.TripoRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindTripoModelByTaskidTest {

    private TripoRepository tripoRepository = mock(TripoRepositoryImpl.class);
    private FindTripoModelByTaskid findTripoModelByTaskid = new FindTripoModelByTaskid(tripoRepository);

    @Test
    public void givenValidTaskIdWhenExecuteThenReturnTripoModel() {
        String taskId = "TASK-001";
        TripoModel model = new TripoModel();
        model.setTaskId(taskId);

        when(tripoRepository.buscarPorTaskId(taskId)).thenReturn(model);

        TripoModel result = findTripoModelByTaskid.execute(taskId);

        assertNotNull(result);
        assertEquals(taskId, result.getTaskId());
        verify(tripoRepository, times(1)).buscarPorTaskId(taskId);
    }

    @Test
    public void givenInavlidTaskIdWhenExecuteThenReturnNull() {
        String taskId = "TASK-002";

        when(tripoRepository.buscarPorTaskId(taskId)).thenReturn(null);
        TripoModel result = findTripoModelByTaskid.execute(taskId);

        assertNull(result);
        verify(tripoRepository, times(1)).buscarPorTaskId(taskId);
    }
}

