package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.dto.BrandDTO;
import com.outfitlab.project.infrastructure.repositories.BrandRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetAllBrandsTest {

    private BrandRepository brandRepository = mock(BrandRepositoryImpl.class);
    private GetAllBrands getAllBrands = new GetAllBrands(brandRepository);

    @Test
    public void givenValidPageWhenBrandsExistThenReturnPageOfBrandDTOs() throws BrandsNotFoundException, PageLessThanZeroException {
        int page = givenValidPage();

        Page<BrandDTO> result = whenBrandsExist(page);

        ThenReturnPageOfBrandDTOs(result, page);
    }

    private void ThenReturnPageOfBrandDTOs(Page<BrandDTO> result, int page) {
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(brandRepository, times(1)).getAllBrands(page);
    }

    private Page<BrandDTO> whenBrandsExist(int page) throws BrandsNotFoundException, PageLessThanZeroException {
        Page<BrandDTO> result = getAllBrands.execute(page);
        return result;
    }

    private int givenValidPage() {
        int page = 0;
        BrandModel brandModel = new BrandModel();
        Page<BrandModel> brandModelsPage = new PageImpl<>(List.of(brandModel), PageRequest.of(page, 10), 1);
        when(brandRepository.getAllBrands(page)).thenReturn(brandModelsPage);
        return page;
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
}

