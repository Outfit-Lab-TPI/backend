package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.domain.useCases.tripo.FindTripoModelByTaskid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindTripoModelByTaskidTest {

    private TripoRepository tripoRepositoryMock;
    private FindTripoModelByTaskid findTripoModelByTaskid;

    @BeforeEach
    public void setUp() {
        tripoRepositoryMock = mock(TripoRepository.class);
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
