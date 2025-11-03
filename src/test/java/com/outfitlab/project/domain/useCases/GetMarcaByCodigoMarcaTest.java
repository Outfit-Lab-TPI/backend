package com.outfitlab.project.domain.useCases;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.dto.BrandAndGarmentsDTO;
import com.outfitlab.project.domain.useCases.marca.GetBrandAndGarmentsByBrandCode;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.model.BrandModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetMarcaByCodigoMarcaTest {

    private BrandRepository marcaRepositoryMock;
    private GetBrandAndGarmentsByBrandCode getMarcaByCodigoMarca;
    private GarmentRepository garmentRepository;

    @BeforeEach
    public void setUp() {
        marcaRepositoryMock = mock(BrandRepository.class);
        getMarcaByCodigoMarca = new GetBrandAndGarmentsByBrandCode(marcaRepositoryMock, garmentRepository);
    }

    @Test
    public void ejecutarDeberiaDevolverMarca_cuandoRepositorioDevuelveMarca() throws BrandsNotFoundException, PageLessThanZeroException {
        BrandModel marca = new BrandModel();
        marca.setNombre("MarcaTest");
        String codigo = "COD123";

        when(marcaRepositoryMock.findByBrandCode(codigo)).thenReturn(marca);

        BrandAndGarmentsDTO resultado = getMarcaByCodigoMarca.execute(codigo, 1);

        assertNotNull(resultado);
        assertEquals("MarcaTest", resultado.getBrandDTO().getNombre());

        verify(marcaRepositoryMock, times(1)).findByBrandCode(codigo);
    }

    @Test
    public void ejecutarDeberiaLanzarExcepcion_cuandoRepositorioNoDevuelveMarca() {
        String codigo = "COD999";

        when(marcaRepositoryMock.findByBrandCode(codigo)).thenReturn(null);

        assertThrows(BrandsNotFoundException.class, () -> {
            getMarcaByCodigoMarca.execute(codigo, 1);
        });
    }
}
