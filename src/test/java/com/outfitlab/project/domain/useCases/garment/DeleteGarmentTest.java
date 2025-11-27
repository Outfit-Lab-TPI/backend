package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.DeleteGarmentException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.PrendaModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteGarmentTest {

    private GarmentRepository garmentRepository;
    private BrandRepository brandRepository;
    private DeleteGarment deleteGarment;

    @BeforeEach
    void setup() {
        garmentRepository = mock(GarmentRepository.class);
        brandRepository = mock(BrandRepository.class);
        deleteGarment = new DeleteGarment(garmentRepository, brandRepository);
    }

    @Test
    void shouldThrowBrandsNotFoundExceptionWhenBrandDoesNotExist() {
        String brandCode = "BR01";
        PrendaModel garment = givenGarment("X1", brandCode);

        whenFindByBrandCodeReturnNull(brandCode);

        whenTryToDeleteThenThrowsBrandNotFoundException(garment, brandCode);
        thenDeleteNeverCalled();
    }

    @Test
    void shouldThrowDeleteGarmentExceptionWhenGarmentBelongsToAnotherBrand() {
        String brandCode = "BR01";
        givenBrandExists(brandCode);

        PrendaModel garment = givenGarment("X1", "OTRA"); // marca distint

        whenTryToDeleteGarmentThenThrowDeleteGarmentException(garment, brandCode);
        thenDeleteNeverCalled();
    }

    private void whenTryToDeleteGarmentThenThrowDeleteGarmentException(PrendaModel garment, String brandCode) {
        assertThrows(
                DeleteGarmentException.class,
                () -> deleteGarment.execute(garment, brandCode)
        );
    }


    @Test
    void shouldDeleteGarmentWhenBrandExistsAndGarmentBelongsToBrand() {
        String brandCode = "BR01";
        String garmentCode = "X1";
        givenBrandExists(brandCode);
        PrendaModel garment = givenGarment(garmentCode, brandCode);

        whenDeleteGarment(garment, brandCode);

        thenGarmentDeletedOnce(garmentCode);
    }

    private void whenDeleteGarment(PrendaModel garment, String brandCode) {
        deleteGarment.execute(garment, brandCode);
    }


    // privadoss
    private PrendaModel givenGarment(String garmentCode, String brandCode) {
        BrandModel marca = new BrandModel();
        marca.setCodigoMarca(brandCode);

        PrendaModel prenda = new PrendaModel();
        prenda.setGarmentCode(garmentCode);
        prenda.setMarca(marca);

        return prenda;
    }

    private void givenBrandExists(String brandCode) {
        BrandModel marca = new BrandModel();
        marca.setCodigoMarca(brandCode);
        when(brandRepository.findByBrandCode(brandCode)).thenReturn(marca);
    }

    private void thenDeleteNeverCalled() {
        verify(garmentRepository, never()).deleteGarment(anyString());
    }

    private void thenGarmentDeletedOnce(String expectedCode) {
        verify(garmentRepository, times(1)).deleteGarment(expectedCode);
    }


    private void whenFindByBrandCodeReturnNull(String brandCode) {
        when(brandRepository.findByBrandCode(brandCode)).thenReturn(null);
    }

    private void whenTryToDeleteThenThrowsBrandNotFoundException(PrendaModel garment, String brandCode) {
        assertThrows(
                BrandsNotFoundException.class,
                () -> deleteGarment.execute(garment, brandCode)
        );
    }
}

