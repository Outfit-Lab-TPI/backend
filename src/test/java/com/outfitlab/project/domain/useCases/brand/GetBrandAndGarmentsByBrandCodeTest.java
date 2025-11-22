package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.dto.BrandAndGarmentsDTO;
import com.outfitlab.project.domain.model.dto.PageDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetBrandAndGarmentsByBrandCodeTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private GarmentRepository garmentRepository;

    @InjectMocks
    private GetBrandAndGarmentsByBrandCode getBrandAndGarmentsByBrandCode;


    @Test
    public void shouldReturnBrandAndGarmentsDTOWhenBrandExistsAndPageIsValid() throws Exception {
        String brandCode = "adidas";
        int page = 1;
        PageDTO upperGarmentsMock = new PageDTO();
        PageDTO lowerGarmentsMock = new PageDTO();

        givenRepositoryCallsAreSuccessful(brandCode, page, upperGarmentsMock, lowerGarmentsMock);

        BrandAndGarmentsDTO result = whenExecuteGetBrandAndGarments(brandCode, page);

        thenResultContainsExpectedData(result, brandCode, page, upperGarmentsMock, lowerGarmentsMock);
    }

    @Test
    public void shouldThrowPageLessThanZeroExceptionWhenPageIsNegative() {
        String brandCode = "nike";
        int invalidPage = -5;

        assertThrows(PageLessThanZeroException.class, () -> getBrandAndGarmentsByBrandCode.execute(brandCode, invalidPage));
        thenRepositoryCallsAreNeverMade();
    }

    @Test
    public void shouldThrowBrandsNotFoundExceptionWhenBrandCodeDoesNotExist() {
        String brandCode = "nonexistent";
        int page = 0;
        givenRepositoryReturnsNullBrand(brandCode);

        assertThrows(BrandsNotFoundException.class, () -> getBrandAndGarmentsByBrandCode.execute(brandCode, page));
        thenBrandRepositoryWasCalled(brandCode, 1);
        thenGarmentRepositoryWasNeverCalled();
    }



    // private methods -----------------------------------

    private void givenRepositoryCallsAreSuccessful(String brandCode, int page, PageDTO upperGarments, PageDTO lowerGarments) {
        when(brandRepository.findByBrandCode(brandCode)).thenReturn(new BrandModel());

        when(garmentRepository.findByBrandCodeAndTipo(eq(brandCode), eq("superior"), eq(page))).thenReturn(upperGarments);
        when(garmentRepository.findByBrandCodeAndTipo(eq(brandCode), eq("inferior"), eq(page))).thenReturn(lowerGarments);
    }

    private void givenRepositoryReturnsNullBrand(String brandCode) {
        when(brandRepository.findByBrandCode(brandCode)).thenReturn(null);
    }


    private BrandAndGarmentsDTO whenExecuteGetBrandAndGarments(String brandCode, int page) throws BrandsNotFoundException, PageLessThanZeroException {
        return getBrandAndGarmentsByBrandCode.execute(brandCode, page);
    }


    private void thenResultContainsExpectedData(BrandAndGarmentsDTO result, String brandCode, int page, PageDTO expectedUpper, PageDTO expectedLower) {
        assertNotNull(result, "El DTO del resultado no debería ser nulo.");
        assertNotNull(result.getBrandDTO(), "El DTO de la marca dentro del resultado no debería ser nulo.");

        assertEquals(expectedUpper, result.getGarmentTop(), "Las prendas superiores deben coincidir con el mock.");
        assertEquals(expectedLower, result.getGarmentBottom(), "Las prendas inferiores deben coincidir con el mock.");

        thenBrandRepositoryWasCalled(brandCode, 1);
        thenGarmentRepositoryWasCalled(brandCode, "superior", page, 1);
        thenGarmentRepositoryWasCalled(brandCode, "inferior", page, 1);
    }

    private void thenBrandRepositoryWasCalled(String brandCode, int times) {
        verify(brandRepository, times(times)).findByBrandCode(brandCode);
    }

    private void thenGarmentRepositoryWasCalled(String brandCode, String tipo, int page, int times) {
        verify(garmentRepository, times(times)).findByBrandCodeAndTipo(brandCode, tipo, page);
    }

    private void thenRepositoryCallsAreNeverMade() {
        verify(brandRepository, never()).findByBrandCode(anyString());
        verify(garmentRepository, never()).findByBrandCodeAndTipo(anyString(), anyString(), anyInt());
    }

    private void thenGarmentRepositoryWasNeverCalled() {
        verify(garmentRepository, never()).findByBrandCodeAndTipo(anyString(), anyString(), anyInt());
    }
}