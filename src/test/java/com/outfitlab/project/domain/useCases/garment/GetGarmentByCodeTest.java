package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.PrendaModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetGarmentByCodeTest {

    private GarmentRepository garmentRepository;
    private GetGarmentByCode getGarmentByCode;

    @BeforeEach
    void setup() {
        garmentRepository = mock(GarmentRepository.class);
        getGarmentByCode = new GetGarmentByCode(garmentRepository);
    }

    @Test
    void shouldReturnGarmentWhenExists() {
        PrendaModel expectedGarment = givenGarment("X1");
        when(garmentRepository.findByGarmentCode("X1")).thenReturn(expectedGarment);

        PrendaModel result = whenGetExecute("X1");

        thenGarmentFound(result, expectedGarment);
    }

    @Test
    void shouldReturnNullWhenGarmentDoesNotExist() {
        when(garmentRepository.findByGarmentCode("NO_EXISTE")).thenReturn(null);

        PrendaModel result = whenGetExecute("NO_EXISTE");

        thenGarmentNotFound(result);
    }



//privados ------------------------------
    private PrendaModel givenGarment(String code) {
        PrendaModel prenda = new PrendaModel();
        prenda.setGarmentCode(code);
        return prenda;
    }

    private void thenGarmentFound(PrendaModel result, PrendaModel expected) {
        assertNotNull(result);
        assertEquals(expected, result);
        verify(garmentRepository, times(1)).findByGarmentCode(expected.getGarmentCode());
    }

    private void thenGarmentNotFound(PrendaModel result) {
        assertNull(result);
        verify(garmentRepository, times(1)).findByGarmentCode(anyString());
    }

    private PrendaModel whenGetExecute(String code) {
        return getGarmentByCode.execute(code);
    }
}
