package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.domain.useCases.tripo.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FindTripoModelByTaskidTest {

    private ITripoRepository tripoRepositoryMock;
    private FindTripoModelByTaskid findTripoModelByTaskid;

    @Before
    public void setUp() {
        tripoRepositoryMock = mock(ITripoRepository.class);
        findTripoModelByTaskid = new FindTripoModelByTaskid(tripoRepositoryMock);
    }

    @Test
    public void ejecutarDeberiaDevolverTripoModel_cuandoRepositorioDevuelveModelo() {
        String taskId = "TASK123";
        TripoModel modeloMock = new TripoModel();
        modeloMock.setImageToken("TripoTest");

        when(tripoRepositoryMock.buscarPorTaskId(taskId)).thenReturn(modeloMock);

        TripoModel resultado = findTripoModelByTaskid.execute(taskId);

        assertNotNull(resultado);
        assertEquals("TripoTest", resultado.getImageToken());

        verify(tripoRepositoryMock, times(1)).buscarPorTaskId(taskId);
    }

    @Test
    public void ejecutarDeberiaDevolverNull_cuandoRepositorioNoDevuelveModelo() {
        String taskId = "TASK999";

        when(tripoRepositoryMock.buscarPorTaskId(taskId)).thenReturn(null);

        TripoModel resultado = findTripoModelByTaskid.execute(taskId);

        assertNull(resultado);

        verify(tripoRepositoryMock, times(1)).buscarPorTaskId(taskId);
    }
}

