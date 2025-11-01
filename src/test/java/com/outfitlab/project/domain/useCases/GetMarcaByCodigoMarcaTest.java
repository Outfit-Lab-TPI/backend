package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.exceptions.MarcasNotFoundException;
import com.outfitlab.project.domain.useCases.marca.GetMarcaByCodigoMarca;
import com.outfitlab.project.domain.interfaces.repositories.IMarcaRepository;
import com.outfitlab.project.domain.model.MarcaModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GetMarcaByCodigoMarcaTest {

    private IMarcaRepository marcaRepositoryMock;
    private GetMarcaByCodigoMarca getMarcaByCodigoMarca;

    @Before
    public void setUp() {
        marcaRepositoryMock = mock(IMarcaRepository.class);
        getMarcaByCodigoMarca = new GetMarcaByCodigoMarca(marcaRepositoryMock);
    }

    @Test
    public void ejecutarDeberiaDevolverMarca_cuandoRepositorioDevuelveMarca() throws MarcasNotFoundException {
        MarcaModel marca = new MarcaModel();
        marca.setNombre("MarcaTest");
        String codigo = "COD123";

        when(marcaRepositoryMock.buscarPorCodigoMarca(codigo)).thenReturn(marca);

        MarcaModel resultado = getMarcaByCodigoMarca.execute(codigo);

        assertNotNull(resultado);
        assertEquals("MarcaTest", resultado.getNombre());

        verify(marcaRepositoryMock, times(1)).buscarPorCodigoMarca(codigo);
    }

    @Test(expected = MarcasNotFoundException.class)
    public void ejecutarDeberiaLanzarExcepcion_cuandoRepositorioNoDevuelveMarca() throws MarcasNotFoundException {
        String codigo = "COD999";

        when(marcaRepositoryMock.buscarPorCodigoMarca(codigo)).thenReturn(null);

        getMarcaByCodigoMarca.execute(codigo);
    }
}
