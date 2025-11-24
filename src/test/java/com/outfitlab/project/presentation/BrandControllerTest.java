package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.model.dto.BrandDTO;
import com.outfitlab.project.domain.useCases.brand.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrandControllerTest {

    private GetAllBrands getAllBrands = mock(GetAllBrands.class);;
    private GetBrandAndGarmentsByBrandCode getBrandAndGarmentsByBrandCode = mock(GetBrandAndGarmentsByBrandCode.class);
    private ActivateBrand activateBrand = mock(ActivateBrand.class);
    private DesactivateBrand desactivateBrand = mock(DesactivateBrand.class);
    private GetAllBrandsWithRelatedUsers getAllBrandsWithRelatedUsers = mock(GetAllBrandsWithRelatedUsers.class);
    private GetNotificationsNewBrands getNotificationsNewBrands = mock(GetNotificationsNewBrands.class);
    private BrandController brandController = new BrandController(getAllBrands, getBrandAndGarmentsByBrandCode, activateBrand, desactivateBrand, getAllBrandsWithRelatedUsers, getNotificationsNewBrands);

    @Test
    public void givenValidPageWhenGetMarcasThenReturnsOkWithContent() throws Exception, BrandsNotFoundException {
        BrandDTO brand1 = new BrandDTO("nike", "Nike",  "https://logos.com/logaso.png");
        BrandDTO brand2 = new BrandDTO("adidas", "Adidas",  "https://logos.com/logaso.png");

        Page<BrandDTO> pageResult = new PageImpl<>(
                List.of(brand1, brand2),
                PageRequest.of(0, 2),
                2
        );

        when(getAllBrands.execute(0)).thenReturn(pageResult);
        ResponseEntity<?> response = brandController.getMarcas(0);


        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals(2L, body.get("totalElements"));
        assertEquals(0, body.get("page"));
        assertNotNull(body.get("content"));

        verify(getAllBrands, times(1)).execute(0);
    }

    @Test
    public void givenNoBrandsFoundWhenGetMarcasThenReturns404() throws Exception, BrandsNotFoundException {
        when(getAllBrands.execute(0)).thenThrow(new BrandsNotFoundException("No se encontraron marcas"));

        ResponseEntity<?> response = brandController.getMarcas(0);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("No se encontraron marcas", response.getBody());
    }

    @Test
    public void givenNegativePageWhenGetMarcasThenReturns404() throws Exception, BrandsNotFoundException {
        when(getAllBrands.execute(-1)).thenThrow(new PageLessThanZeroException("Página menor que cero"));

        ResponseEntity<?> response = brandController.getMarcas(-1);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Página menor que cero", response.getBody());
    }

    @Test
    public void givenNoExistingBrandWhenGetBrandAndGarmentsByBrandCodeThenReturns404() throws Exception, BrandsNotFoundException {
        when(getBrandAndGarmentsByBrandCode.execute("puma", 0)).thenThrow(new BrandsNotFoundException("Marca no encontrada"));

        ResponseEntity<?> response = brandController.getBrandAndGarmentsByBrandCode("puma", 0);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Marca no encontrada", response.getBody());
    }

    @Test
    public void givenNegativePageWhenGetBrandAndGarmentsByBrandCodeThenReturns404() throws Exception, BrandsNotFoundException {
        when(getBrandAndGarmentsByBrandCode.execute("nike", -1)).thenThrow(new PageLessThanZeroException("Página menor que cero"));

        ResponseEntity<?> response = brandController.getBrandAndGarmentsByBrandCode("nike", -1);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Página menor que cero", response.getBody());
    }
}

