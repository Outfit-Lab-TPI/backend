package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.BrandModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ActivateBrandTest {

    private BrandRepository brandRepository = mock(BrandRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ActivateBrand activateBrand;

    private final String BRAND_CODE = "adidas";
    private final String USER_EMAIL = "adidas@corp.com";
    private final String SUCCESS_MESSAGE = "Marca activada con éxito.";

    @BeforeEach
    void setUp() {
        activateBrand = new ActivateBrand(brandRepository, userRepository);
    }


    @Test
    public void shouldActivateUserAndReturnSuccessMessageWhenBrandExists() throws BrandsNotFoundException {
        givenBrandExistsAndUserEmailIsAvailable(BRAND_CODE, USER_EMAIL);

        String result = whenExecuteActivateBrand(BRAND_CODE);

        thenResultIsSuccessMessage(result);
        thenUserWasActivated(USER_EMAIL);
    }

    @Test
    public void shouldThrowBrandsNotFoundExceptionWhenBrandDoesNotExist() {
        givenBrandDoesNotExist(BRAND_CODE);

        assertThrows(BrandsNotFoundException.class, () -> activateBrand.execute(BRAND_CODE));

        thenActivationWasNeverCalled();
    }


    //privadosss
    private void givenBrandExistsAndUserEmailIsAvailable(String brandCode, String userEmail) {
        when(brandRepository.findByBrandCode(brandCode)).thenReturn(new BrandModel());
        when(userRepository.getEmailUserRelatedToBrandByBrandCode(brandCode)).thenReturn(userEmail);
    }

    private void givenBrandDoesNotExist(String brandCode) {
        when(brandRepository.findByBrandCode(brandCode)).thenReturn(null);
    }

    private String whenExecuteActivateBrand(String brandCode) {
        return activateBrand.execute(brandCode);
    }

    private void thenResultIsSuccessMessage(String result) {
        assertEquals(SUCCESS_MESSAGE, result, "El mensaje de retorno debe ser de activación exitosa.");
    }

    private void thenUserWasActivated(String userEmail) {
        verify(userRepository, times(1)).getEmailUserRelatedToBrandByBrandCode(BRAND_CODE);
        verify(userRepository, times(1)).activateUser(userEmail);
    }

    private void thenActivationWasNeverCalled() {
        verify(userRepository, never()).activateUser(anyString());
    }
}