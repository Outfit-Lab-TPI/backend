package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.dto.BrandDTO;
import com.outfitlab.project.domain.useCases.marca.GetAllBrands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetAllMarcasTest {

    private BrandRepository marcaRepositoryMock;
    private GetAllBrands getAllMarcas;

    @BeforeEach
    public void setUp() {
        marcaRepositoryMock = mock(BrandRepository.class);
        getAllMarcas = new GetAllBrands(marcaRepositoryMock);
    }

    /*@Test
    public void ejecutar_deberiaDevolverListaDeMarcas_cuandoElRepositorioTieneMarcas() throws BrandsNotFoundException, PageLessThanZeroException {
        BrandModel marca1 = new BrandModel();
        marca1.setNombre("Marca 1");

        BrandModel marca2 = new BrandModel();
        marca2.setNombre("Marca 2");

        List<BrandModel> marcasMock = Arrays.asList(marca1, marca2);

        when(marcaRepositoryMock.getAllBrands(1)).thenReturn(marcasMock);
        List<BrandDTO> resultado = getAllMarcas.execute(1);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Marca 1", resultado.get(0).getNombre());
        assertEquals("Marca 2", resultado.get(1).getNombre());
    }*/

    /*@Test
    public void ejecutar_deberiaLanzarExcepcion_cuandoElRepositorioNoTieneMarcas() {
        when(marcaRepositoryMock.getAllBrands(1)).thenReturn(Collections.emptyList());

        assertThrows(BrandsNotFoundException.class, () -> {
            getAllMarcas.execute(1);
        });
    }*/
}
