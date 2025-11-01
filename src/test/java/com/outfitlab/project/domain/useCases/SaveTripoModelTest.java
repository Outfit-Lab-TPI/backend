package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.domain.useCases.tripo.SaveTripoModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SaveTripoModelTest {

    private ITripoRepository tripoRepositoryMock;
    private SaveTripoModel saveTripoModel;

    @Before
    public void setUp() {
        tripoRepositoryMock = mock(ITripoRepository.class);
        saveTripoModel = new SaveTripoModel(tripoRepositoryMock);
    }

    @Test
    public void ejecutarDeberiaGuardarYDevolverTripoModel_cuandoDatosSonValidos() {
        TripoModel modelo = new TripoModel();
        modelo.setImageToken("TripoTest");

        when(tripoRepositoryMock.save(modelo)).thenReturn(modelo);

        TripoModel resultado = saveTripoModel.execute(modelo);

        assertNotNull(resultado);
        assertEquals("TripoTest", resultado.getImageToken());

        verify(tripoRepositoryMock, times(1)).save(modelo);
    }

    @Test
    public void ejecutarDeberiaDevolverNull_cuandoRepositorioDevuelveNull() {
        TripoModel modelo = new TripoModel();

        when(tripoRepositoryMock.save(modelo)).thenReturn(null);

        TripoModel resultado = saveTripoModel.execute(modelo);

        assertNull(resultado);
        verify(tripoRepositoryMock, times(1)).save(modelo);
    }
}

