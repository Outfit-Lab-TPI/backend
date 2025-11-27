package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.BrandModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DesactivateBrandTest {

    private BrandRepository brandRepository = mock(BrandRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private DesactivateBrand desactivateBrand;

    private final String BRAND_CODE = "nike";
    private final String USER_EMAIL = "nike@corp.com";
    private final String SUCCESS_MESSAGE = "Marca desactivada con éxito.";

    @BeforeEach
    void setUp() {
        desactivateBrand = new DesactivateBrand(brandRepository, userRepository);
    }


    @Test
    public void shouldDesactivateUserAndReturnSuccessMessageWhenBrandExists() throws BrandsNotFoundException {
        givenBrandExistsAndUserEmailIsAvailable(BRAND_CODE, USER_EMAIL);

        String result = whenExecuteDesactivateBrand(BRAND_CODE);

        thenResultIsSuccessMessage(result);
        thenUserWasDesactivated(USER_EMAIL);
    }

    @Test
    public void shouldThrowBrandsNotFoundExceptionWhenBrandDoesNotExist() {
        givenBrandDoesNotExist(BRAND_CODE);

        assertThrows(BrandsNotFoundException.class, () -> desactivateBrand.execute(BRAND_CODE));

        thenDesactivationWasNeverCalled();
    }


    //privadoss
    private void givenBrandExistsAndUserEmailIsAvailable(String brandCode, String userEmail) {
        when(brandRepository.findByBrandCode(brandCode)).thenReturn(new BrandModel());
        when(userRepository.getEmailUserRelatedToBrandByBrandCode(brandCode)).thenReturn(userEmail);
    }

    private void givenBrandDoesNotExist(String brandCode) {
        when(brandRepository.findByBrandCode(brandCode)).thenReturn(null);
    }

    private String whenExecuteDesactivateBrand(String brandCode) {
        return desactivateBrand.execute(brandCode);
    }

    private void thenResultIsSuccessMessage(String result) {
        assertEquals(SUCCESS_MESSAGE, result, "El mensaje de retorno debe ser de desactivación exitosa.");
    }

    private void thenUserWasDesactivated(String userEmail) {
        verify(userRepository, times(1)).getEmailUserRelatedToBrandByBrandCode(BRAND_CODE);
        verify(userRepository, times(1)).desactivateUser(userEmail);
    }

    private void thenDesactivationWasNeverCalled() {
        verify(userRepository, never()).desactivateUser(anyString());
    }
}