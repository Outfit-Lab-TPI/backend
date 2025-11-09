package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.dto.BrandDTO;
import com.outfitlab.project.infrastructure.repositories.BrandRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAllBrandsTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private GetAllBrands getAllBrands;
    //test de use case nombrarlos con logica de negocio

    @Test
    public void shouldReturnAllBrands(){
        givenExistsBrands(10);
        Page<BrandDTO> result = whenGetBrands(10);
        thenReturnPageOfBrand(result, 10);
    }

    @Test
    public void givenNegativePageWhenExecuteThenThrowPageLessThanZeroException() {
        int invalidPage = -1;

        assertThrows(PageLessThanZeroException.class, () -> getAllBrands.execute(invalidPage));
        verify(brandRepository, never()).getAllBrands(anyInt());
    }

    @Test
    public void givenValidPageWhenNoBrandsExistThenThrowBrandsNotFoundException() {
        int page = 0;
        when(brandRepository.getAllBrands(page)).thenReturn(Page.empty());

        assertThrows(BrandsNotFoundException.class, () -> getAllBrands.execute(page));
        verify(brandRepository, times(1)).getAllBrands(page);
    }







    // private methods -----------------------------------
    private void thenReturnPageOfBrand(Page<BrandDTO> result, int expected) {
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(brandRepository, times(1)).getAllBrands(expected);
    }

    private Page<BrandDTO> whenGetBrands(int amount) throws BrandsNotFoundException, PageLessThanZeroException {
        Page<BrandDTO> result = getAllBrands.execute(amount);
        return result;
    }

    private int givenExistsBrands(int size) {
        BrandModel brandModel = new BrandModel();
        Page<BrandModel> brandModelsPage = new PageImpl<>(List.of(brandModel), PageRequest.of(size, size), 1);
        when(brandRepository.getAllBrands(size)).thenReturn(brandModelsPage);
        return size;
    }
}

