package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.UpdateGarmentException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.BrandModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UpdateGarmentTest {

    private GarmentRepository garmentRepository;
    private BrandRepository brandRepository;
    private UpdateGarment useCase;

    @BeforeEach
    void setUp() {
        garmentRepository = mock(GarmentRepository.class);
        brandRepository = mock(BrandRepository.class);
        useCase = new UpdateGarment(garmentRepository, brandRepository);
    }

    @Test
    void debeActualizarCorrectamenteLaPrenda() {
        PrendaModel garment = givenGarmentDeLaMismaMarca();
        givenMarcaExistente();
        givenRepositorioDevuelvePrenda(garment);

        whenEjecutaUseCase();

        thenDebeLlamarseUpdateGarment();
    }

    @Test
    void debeLanzarExcepcionCuandoLaMarcaNoExiste() {
        givenMarcaNoExiste();
        assertThrows(BrandsNotFoundException.class, this::whenEjecutaUseCase);
    }

    @Test
    void debeLanzarExcepcionCuandoLaPrendaEsDeOtraMarca() {
        PrendaModel garment = givenGarmentDeOtraMarca();
        givenMarcaExistente();
        givenRepositorioDevuelvePrenda(garment);

        assertThrows(UpdateGarmentException.class, this::whenEjecutaUseCase);
    }

//privados ----
    private void givenMarcaExistente() {
        BrandModel marca = mock(BrandModel.class);
        when(brandRepository.findByBrandCode(eq("BRAND123"))).thenReturn(marca);
    }

    private void givenMarcaNoExiste() {
        when(brandRepository.findByBrandCode(eq("BRAND123"))).thenReturn(null);
    }

    private void givenRepositorioDevuelvePrenda(PrendaModel garment) {
        when(garmentRepository.findByGarmentCode(eq("ABC123")))
                .thenReturn(garment);
    }

    private PrendaModel givenGarmentDeLaMismaMarca() {
        BrandModel marca = mock(BrandModel.class);
        when(marca.getCodigoMarca()).thenReturn("BRAND123");

        PrendaModel garment = mock(PrendaModel.class);
        when(garment.getMarca()).thenReturn(marca);
        when(garment.getGarmentCode()).thenReturn("ABC123");

        return garment;
    }

    private PrendaModel givenGarmentDeOtraMarca() {
        BrandModel marca = mock(BrandModel.class);
        when(marca.getCodigoMarca()).thenReturn("OTRA");

        PrendaModel garment = mock(PrendaModel.class);
        when(garment.getMarca()).thenReturn(marca);
        when(garment.getGarmentCode()).thenReturn("ABC123");

        return garment;
    }

    private void whenEjecutaUseCase() {
        useCase.execute(
                "Nombre",
                "tipo",
                "color",
                "evento",
                "ABC123",
                "BRAND123",
                "img.png",
                "clima",
                List.of("formal"),
                "genero"
        );
    }

    private void thenDebeLlamarseUpdateGarment() {
        verify(garmentRepository, times(1))
                .updateGarment(
                        eq("Nombre"),
                        eq("tipo"),
                        eq("color"),
                        eq("evento"),
                        eq("ABC123"),
                        eq("img.png"),
                        anyString(), // formatGarmentCode() -> validado
                        eq("clima"),
                        eq(List.of("formal")),
                        eq("genero")
                );
    }
}
