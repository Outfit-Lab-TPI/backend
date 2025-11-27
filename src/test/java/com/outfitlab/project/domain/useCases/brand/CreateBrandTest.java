package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.model.BrandModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateBrandTest {

    private BrandRepository brandRepository = mock(BrandRepository.class);
    private CreateBrand createBrand;

    private final String BRAND_NAME = "Zara Shop";
    private final String LOGO_URL = "http://logo.com/zara-shop.png";
    private final String URL_SITE = "http://zarashop.com";
    private final String EXPECTED_BRAND_CODE = "zara_shop";
    private final String EXPECTED_RESPONSE = "brand-id-123";

    @BeforeEach
    void setUp() {
        createBrand = new CreateBrand(brandRepository);
    }


    @Test
    public void shouldCreateBrandModelWithFormattedCodeAndCallRepository() {
        givenRepositoryReturnsExpectedResponse(EXPECTED_RESPONSE);

        String result = whenExecuteCreateBrand(BRAND_NAME, LOGO_URL, URL_SITE);

        thenRepositoryWasCalledWithCorrectBrandModel(BRAND_NAME, LOGO_URL, URL_SITE, EXPECTED_BRAND_CODE);
        thenResultIsExpected(result, EXPECTED_RESPONSE);
    }


    //privadoss
    private void givenRepositoryReturnsExpectedResponse(String response) {
        when(brandRepository.createBrand(any(BrandModel.class))).thenReturn(response);
    }

    private String whenExecuteCreateBrand(String brandName, String logoUrl, String urlSite) {
        return createBrand.execute(brandName, logoUrl, urlSite);
    }

    private void thenResultIsExpected(String actualResult, String expectedResponse) {
        assertEquals(expectedResponse, actualResult, "El resultado debe ser la respuesta del repositorio.");
    }

    private void thenRepositoryWasCalledWithCorrectBrandModel(String expectedName, String expectedLogoUrl, String expectedUrlSite, String expectedCode) {
        ArgumentCaptor<BrandModel> captor = ArgumentCaptor.forClass(BrandModel.class);
        verify(brandRepository, times(1)).createBrand(captor.capture());

        BrandModel capturedModel = captor.getValue();

        assertEquals(expectedName, capturedModel.getNombre(), "El nombre de la marca debe coincidir.");
        assertEquals(expectedLogoUrl, capturedModel.getLogoUrl(), "La URL del logo debe coincidir.");
        assertEquals(expectedUrlSite, capturedModel.getUrlSite(), "La URL del sitio debe coincidir.");
        assertEquals(expectedCode, capturedModel.getCodigoMarca(), "El código de marca debe estar formateado.");
    }
}