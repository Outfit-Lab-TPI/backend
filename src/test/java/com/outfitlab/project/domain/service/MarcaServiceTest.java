package com.outfitlab.project.domain.service;

import com.outfitlab.project.domain.exceptions.MarcasNotFoundException;
import com.outfitlab.project.domain.model.MarcaModel;
import com.outfitlab.project.domain.useCases.marca.GetAllMarcas;
import com.outfitlab.project.domain.useCases.marca.GetMarcaByCodigoMarca;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MarcaServiceTest {

    private GetAllMarcas getAllMarcasMock;
    private GetMarcaByCodigoMarca getMarcaByCodigoMarcaMock;
    private MarcaService marcaService;

    @Before
    public void setUp() {
        getAllMarcasMock = mock(GetAllMarcas.class);
        getMarcaByCodigoMarcaMock = mock(GetMarcaByCodigoMarca.class);
        marcaService = new MarcaService(getAllMarcasMock, getMarcaByCodigoMarcaMock);
    }

    @Test
    public void getAllMarcasDeberiaDevolverListaDeMarcas_cuandoExistenMarcas() throws Exception, MarcasNotFoundException {
        MarcaModel marca1 = new MarcaModel();
        marca1.setNombre("Marca1");
        MarcaModel marca2 = new MarcaModel();
        marca2.setNombre("Marca2");

        when(getAllMarcasMock.execute()).thenReturn(Arrays.asList(marca1, marca2));

        List<MarcaModel> resultado = null;
        try {
            resultado = marcaService.getAllMarcas();
        } catch (MarcasNotFoundException e) {
            fail("No se esperaba excepción");
        }

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(getAllMarcasMock, times(1)).execute();
    }

    @Test
    public void getAllMarcasDeberiaLanzarMarcasNotFoundException_cuandoUseCaseLanzaExcepcion() throws Exception, MarcasNotFoundException {
        when(getAllMarcasMock.execute()).thenThrow(new MarcasNotFoundException("No marcas"));

        try {
            marcaService.getAllMarcas();
            fail("Se esperaba MarcasNotFoundException");
        } catch (MarcasNotFoundException e) {
            assertEquals("No marcas", e.getMessage());
        }
    }

    @Test
    public void getAllMarcasDeberiaLanzarMarcasNotFoundException_cuandoUseCaseLanzaExceptionGenerica() throws Exception, MarcasNotFoundException {
        when(getAllMarcasMock.execute()).thenThrow(new RuntimeException("Error genérico"));

        try {
            marcaService.getAllMarcas();
            fail("Se esperaba MarcasNotFoundException");
        } catch (MarcasNotFoundException e) {
            assertEquals("Error genérico", e.getMessage());
        }
    }

    @Test
    public void getMarcaByCodigoMarcaDeberiaDevolverMarca_cuandoExiste() throws Exception, MarcasNotFoundException {
        String codigo = "MAR001";
        MarcaModel marca = new MarcaModel();
        marca.setNombre("Marca1");

        when(getMarcaByCodigoMarcaMock.execute(codigo)).thenReturn(marca);

        MarcaModel resultado = null;
        try {
            resultado = marcaService.getMarcaByCodigoMarca(codigo);
        } catch (MarcasNotFoundException e) {
            fail("No se esperaba excepción");
        }

        assertNotNull(resultado);
        assertEquals("Marca1", resultado.getNombre());
        verify(getMarcaByCodigoMarcaMock, times(1)).execute(codigo);
    }

    @Test
    public void getMarcaByCodigoMarcaDeberiaLanzarMarcasNotFoundException_cuandoUseCaseLanzaExcepcion() throws Exception, MarcasNotFoundException {
        String codigo = "MAR002";
        when(getMarcaByCodigoMarcaMock.execute(codigo)).thenThrow(new MarcasNotFoundException("No encontramos la marca: MAR002"));

        try {
            marcaService.getMarcaByCodigoMarca(codigo);
            fail("Se esperaba MarcasNotFoundException");
        } catch (MarcasNotFoundException e) {
            assertEquals("No encontramos la marca: " + codigo, e.getMessage());
        }
    }

    @Test
    public void getMarcaByCodigoMarcaDeberiaLanzarMarcasNotFoundException_cuandoUseCaseLanzaExceptionGenerica() throws Exception, MarcasNotFoundException {
        String codigo = "MAR003";
        when(getMarcaByCodigoMarcaMock.execute(codigo)).thenThrow(new RuntimeException("Error genérico"));

        try {
            marcaService.getMarcaByCodigoMarca(codigo);
            fail("Se esperaba MarcasNotFoundException");
        } catch (MarcasNotFoundException e) {
            assertEquals("No encontramos la marca: " + codigo, e.getMessage());
        }
    }
}

