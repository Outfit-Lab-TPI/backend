package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.infrastructure.repositories.GarmentRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetGarmentsByTypeTest {

    private GarmentRepository garmentRepository = mock(GarmentRepositoryImpl.class);
    private GetGarmentsByType getGarmentsByType = new GetGarmentsByType(garmentRepository);

    @Test
    public void givenValidTypeWithGarmentsWhenExecuteThenReturnPageSuccessfully() throws GarmentNotFoundException {
        String type = "superior";
        int page = 0;
        Page<PrendaModel> pageResponse = new PageImpl<>(List.of(new PrendaModel(), new PrendaModel()));

        when(garmentRepository.getGarmentsByType(type.toLowerCase(), page)).thenReturn(pageResponse);

        Page<PrendaModel> result = getGarmentsByType.execute(type, page);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(garmentRepository, times(1)).getGarmentsByType("superior", page);
    }

    @Test
    public void givenValidTypeWithNoGarmentsWhenExecuteThenThrowGarmentNotFoundException() {
        String type = "inferior";
        int page = 1;
        Page<PrendaModel> emptyPage = new PageImpl<>(List.of());

        when(garmentRepository.getGarmentsByType(type.toLowerCase(), page)).thenReturn(emptyPage);

        GarmentNotFoundException exception = assertThrows(GarmentNotFoundException.class, () -> getGarmentsByType.execute(type, page));

        assertTrue(exception.getMessage().contains("No encontramos prendas de tipo: " + type));
        verify(garmentRepository, times(1)).getGarmentsByType("inferior", page);
    }

    @Test
    public void givenEmptyPageWhenExecuteThenThrowGarmentNotFoundException() {
        String type = "accesorio";
        int page = 2;
        Page<PrendaModel> emptyPage = new PageImpl<>(List.of());

        when(garmentRepository.getGarmentsByType(type.toLowerCase(), page)).thenReturn(emptyPage);

        assertThrows(GarmentNotFoundException.class, () -> getGarmentsByType.execute(type, page));
        verify(garmentRepository, times(1)).getGarmentsByType("accesorio", page);
    }

}

