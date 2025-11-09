package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.dto.BrandAndGarmentsDTO;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.infrastructure.repositories.BrandRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.GarmentRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetBrandAndGarmentsByBrandCodeTest {

    private BrandRepository brandRepository = mock(BrandRepositoryImpl.class);
    private GarmentRepository garmentRepository = mock(GarmentRepositoryImpl.class);
    private GetBrandAndGarmentsByBrandCode getBrandAndGarmentsByBrandCode = new GetBrandAndGarmentsByBrandCode(brandRepository, garmentRepository);

    @Test
    public void givenValidBrandCodeAndPageWhenExecuteThenReturnBrandAndGarmentsDTO() throws BrandsNotFoundException, PageLessThanZeroException {
        String brandCode = "nike";
        int page = 0;
        BrandModel brandModel = new BrandModel();
        PageDTO upperGarments = new PageDTO();
        PageDTO lowerGarments = new PageDTO();
        when(brandRepository.findByBrandCode(brandCode)).thenReturn(brandModel);
        when(garmentRepository.findByBrandCodeAndTipo(brandCode, "superior", page)).thenReturn(upperGarments);
        when(garmentRepository.findByBrandCodeAndTipo(brandCode, "inferior", page)).thenReturn(lowerGarments);

        BrandAndGarmentsDTO result = getBrandAndGarmentsByBrandCode.execute(brandCode, page);

        thenReturnBrandAndGarmentsDTO(result, upperGarments, lowerGarments, brandCode, page);
    }

    private void thenReturnBrandAndGarmentsDTO(BrandAndGarmentsDTO result, PageDTO upperGarments, PageDTO lowerGarments, String brandCode, int page) {
        assertNotNull(result);
        assertNotNull(result.getBrandDTO());
        assertEquals(upperGarments, result.getGarmentTop());
        assertEquals(lowerGarments, result.getGarmentBottom());
        verify(brandRepository, times(1)).findByBrandCode(brandCode);
        verify(garmentRepository, times(1)).findByBrandCodeAndTipo(brandCode, "superior", page);
        verify(garmentRepository, times(1)).findByBrandCodeAndTipo(brandCode, "inferior", page);
    }

    @Test
    public void givenNegativePageWhenExecuteThenThrowPageLessThanZeroException() {
        String brandCode = "nike";
        int invalidPage = -1;

        assertThrows(PageLessThanZeroException.class, () -> getBrandAndGarmentsByBrandCode.execute(brandCode, invalidPage));
        verify(brandRepository, never()).findByBrandCode(anyString());
        verify(garmentRepository, never()).findByBrandCodeAndTipo(anyString(), anyString(), anyInt());
    }

    @Test
    public void givenNonexistentBrandCodeWhenExecuteThenThrowBrandsNotFoundException() {
        String brandCode = "cualquiera";
        int page = 0;
        when(brandRepository.findByBrandCode(brandCode)).thenReturn(null);

        assertThrows(BrandsNotFoundException.class, () -> getBrandAndGarmentsByBrandCode.execute(brandCode, page));
        verify(brandRepository, times(1)).findByBrandCode(brandCode);
    }
}

