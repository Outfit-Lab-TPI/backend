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

    private GetAllBrands getAllBrands = mock(GetAllBrands.class);
    private GetBrandAndGarmentsByBrandCode getBrandAndGarmentsByBrandCode = mock(GetBrandAndGarmentsByBrandCode.class);
    private ActivateBrand activateBrand = mock(ActivateBrand.class);
    private DesactivateBrand desactivateBrand = mock(DesactivateBrand.class);
    private GetAllBrandsWithRelatedUsers getAllBrandsWithRelatedUsers = mock(GetAllBrandsWithRelatedUsers.class);
    private GetNotificationsNewBrands getNotificationsNewBrands = mock(GetNotificationsNewBrands.class);

    private BrandController controller = new BrandController(
                    getAllBrands,
                    getBrandAndGarmentsByBrandCode,
                    activateBrand,
                    desactivateBrand,
                    getAllBrandsWithRelatedUsers,
                    getNotificationsNewBrands);

    @Test
    void givenValidPageWhenGetMarcasThenReturnOk() throws Exception {
        givenBrandsExist();
        ResponseEntity<?> response = whenCallGetMarcas(0);
        thenResponseOkWithBrands(response);
        thenGetAllBrandsCalledOnce();
    }

    @Test
    void givenNoBrandsWhenGetMarcasThenReturn404() throws Exception {
        givenBrandsNotFound();
        ResponseEntity<?> response = whenCallGetMarcas(0);
        thenResponseNotFound(response, "No se encontraron marcas");
    }

    @Test
    void givenNegativePageWhenGetMarcasThenReturn404() throws Exception {
        givenNegativePageError();
        ResponseEntity<?> response = whenCallGetMarcas(-1);
        thenResponseNotFound(response, "Página menor que cero");
    }

    @Test
    void givenNonExistingBrandWhenGetBrandGarmentsThenReturn404() throws Exception {
        givenBrandNotFoundForGetBrandGarments();
        ResponseEntity<?> response = whenCallGetBrandAndGarments("puma", 0);
        thenResponseNotFound(response, "Marca no encontrada");
    }

    @Test
    void givenNegativePageWhenGetBrandGarmentsThenReturn404() throws Exception {
        givenNegativePageErrorForBrandGarments();
        ResponseEntity<?> response = whenCallGetBrandAndGarments("nike", -1);
        thenResponseNotFound(response, "Página menor que cero");
    }

// privadosss ----
    private void givenBrandsExist() throws Exception {
        BrandDTO brand1 = new BrandDTO("nike", "Nike", "https://logos.com/logaso.png");
        BrandDTO brand2 = new BrandDTO("adidas", "Adidas", "https://logos.com/logaso.png");

        Page<BrandDTO> pageResult = new PageImpl<>(
                List.of(brand1, brand2),
                PageRequest.of(0, 2),
                2
        );

        when(getAllBrands.execute(0)).thenReturn(pageResult);
    }

    private void givenBrandsNotFound() throws Exception {
        when(getAllBrands.execute(0))
                .thenThrow(new BrandsNotFoundException("No se encontraron marcas"));
    }

    private void givenNegativePageError() throws Exception {
        when(getAllBrands.execute(-1))
                .thenThrow(new PageLessThanZeroException("Página menor que cero"));
    }

    private void givenBrandNotFoundForGetBrandGarments() throws Exception {
        when(getBrandAndGarmentsByBrandCode.execute("puma", 0))
                .thenThrow(new BrandsNotFoundException("Marca no encontrada"));
    }

    private void givenNegativePageErrorForBrandGarments() throws Exception {
        when(getBrandAndGarmentsByBrandCode.execute("nike", -1))
                .thenThrow(new PageLessThanZeroException("Página menor que cero"));
    }

    private ResponseEntity<?> whenCallGetMarcas(int page) {
        return controller.getMarcas(page);
    }

    private ResponseEntity<?> whenCallGetBrandAndGarments(String brand, int page) {
        return controller.getBrandAndGarmentsByBrandCode(brand, page);
    }

    private void thenResponseOkWithBrands(ResponseEntity<?> response) {
        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals(2L, body.get("totalElements"));
        assertEquals(0, body.get("page"));
        assertNotNull(body.get("content"));
    }

    private void thenGetAllBrandsCalledOnce() {
        verify(getAllBrands, times(1)).execute(0);
    }

    private void thenResponseNotFound(ResponseEntity<?> response, String expectedMessage) {
        assertEquals(404, response.getStatusCode().value());
        assertEquals(expectedMessage, response.getBody());
    }
}
