package com.outfitlab.project.domain.service;

import com.outfitlab.project.domain.exceptions.MarcasNotFoundException;
import com.outfitlab.project.domain.model.MarcaModel;
import com.outfitlab.project.domain.useCases.marca.GetAllMarcas;
import com.outfitlab.project.domain.useCases.marca.GetMarcaByCodigoMarca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MarcaServiceTest {

    private GetAllMarcas getAllMarcasMock;
    private GetMarcaByCodigoMarca getMarcaByCodigoMarcaMock;
    private MarcaService marcaService;

    @BeforeEach
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

        List<MarcaModel> resultado = marcaService.getAllMarcas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(getAllMarcasMock, times(1)).execute();
    }

    @Test
    public void getAllMarcasDeberiaLanzarMarcasNotFoundException_cuandoUseCaseLanzaExcepcion() throws Exception, MarcasNotFoundException {
        when(getAllMarcasMock.execute()).thenThrow(new MarcasNotFoundException("No marcas"));

        MarcasNotFoundException ex = assertThrows(MarcasNotFoundException.class,
                () -> marcaService.getAllMarcas());

        assertEquals("No marcas", ex.getMessage());
    }

    @Test
    public void getAllMarcasDeberiaLanzarMarcasNotFoundException_cuandoUseCaseLanzaExceptionGenerica() throws Exception, MarcasNotFoundException {
        when(getAllMarcasMock.execute()).thenThrow(new RuntimeException("Error genérico"));

        MarcasNotFoundException ex = assertThrows(MarcasNotFoundException.class,
                () -> marcaService.getAllMarcas());

        assertEquals("Error genérico", ex.getMessage());
    }

    @Test
    public void getMarcaByCodigoMarcaDeberiaDevolverMarca_cuandoExiste() throws Exception, MarcasNotFoundException {
        String codigo = "MAR001";
        MarcaModel marca = new MarcaModel();
        marca.setNombre("Marca1");

        when(getMarcaByCodigoMarcaMock.execute(codigo)).thenReturn(marca);

        MarcaModel resultado = marcaService.getMarcaByCodigoMarca(codigo);

        assertNotNull(resultado);
        assertEquals("Marca1", resultado.getNombre());
        verify(getMarcaByCodigoMarcaMock, times(1)).execute(codigo);
    }

    @Test
    public void getMarcaByCodigoMarcaDeberiaLanzarMarcasNotFoundException_cuandoUseCaseLanzaExcepcion() throws Exception, MarcasNotFoundException {
        String codigo = "MAR002";
        when(getMarcaByCodigoMarcaMock.execute(codigo))
                .thenThrow(new MarcasNotFoundException("No encontramos la marca: MAR002"));

        MarcasNotFoundException ex = assertThrows(MarcasNotFoundException.class,
                () -> marcaService.getMarcaByCodigoMarca(codigo));

        assertEquals("No encontramos la marca: " + codigo, ex.getMessage());
    }

    @Test
    public void getMarcaByCodigoMarcaDeberiaLanzarMarcasNotFoundException_cuandoUseCaseLanzaExceptionGenerica() throws Exception, MarcasNotFoundException {
        String codigo = "MAR003";
        when(getMarcaByCodigoMarcaMock.execute(codigo)).thenThrow(new RuntimeException("Error genérico"));

        MarcasNotFoundException ex = assertThrows(MarcasNotFoundException.class,
                () -> marcaService.getMarcaByCodigoMarca(codigo));

        assertEquals("No encontramos la marca: " + codigo, ex.getMessage());
    }
}
