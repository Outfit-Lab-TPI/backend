package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.exceptions.MarcasNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.MarcaRepository;
import com.outfitlab.project.domain.model.MarcaModel;
import com.outfitlab.project.domain.useCases.marca.GetAllMarcas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetAllMarcasTest {

    private MarcaRepository marcaRepositoryMock;
    private GetAllMarcas getAllMarcas;

    @BeforeEach
    public void setUp() {
        marcaRepositoryMock = mock(MarcaRepository.class);
        getAllMarcas = new GetAllMarcas(marcaRepositoryMock);
    }

    @Test
    public void ejecutar_deberiaDevolverListaDeMarcas_cuandoElRepositorioTieneMarcas() throws MarcasNotFoundException {
        MarcaModel marca1 = new MarcaModel();
        marca1.setNombre("Marca 1");

        MarcaModel marca2 = new MarcaModel();
        marca2.setNombre("Marca 2");

        List<MarcaModel> marcasMock = Arrays.asList(marca1, marca2);

        when(marcaRepositoryMock.obtenerTodas()).thenReturn(marcasMock);
        List<MarcaModel> resultado = getAllMarcas.execute();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Marca 1", resultado.get(0).getNombre());
        assertEquals("Marca 2", resultado.get(1).getNombre());
    }

    @Test
    public void ejecutar_deberiaLanzarExcepcion_cuandoElRepositorioNoTieneMarcas() {
        when(marcaRepositoryMock.obtenerTodas()).thenReturn(Collections.emptyList());

        assertThrows(MarcasNotFoundException.class, () -> {
            getAllMarcas.execute();
        });
    }
}
