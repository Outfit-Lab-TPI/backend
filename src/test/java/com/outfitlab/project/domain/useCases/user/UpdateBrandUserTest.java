package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UpdateBrandUserTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private UpdateBrandUser updateBrandUser;

    private final String VALID_EMAIL = "brand.user@test.com";
    private final String VALID_BRAND_CODE = "NEW-BRAND-123";
    private final String NULL_EMAIL = null;
    private final String EMPTY_CODE = "";

    @BeforeEach
    void setUp() {
        updateBrandUser = new UpdateBrandUser(userRepository);
    }


    @Test
    public void shouldCallRepositoryToUpdateBrandUserWithValidData() {
        whenExecuteUpdateBrandUser(VALID_EMAIL, VALID_BRAND_CODE);

        thenRepositoryUpdateWasCalled(VALID_EMAIL, VALID_BRAND_CODE, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenUserEmailIsNull() {
        givenRepositoryThrowsRuntimeException(NULL_EMAIL, VALID_BRAND_CODE);

        assertThrows(RuntimeException.class,
                () -> whenExecuteUpdateBrandUser(NULL_EMAIL, VALID_BRAND_CODE),
                "Se espera que el RuntimeException se propague al delegar un email nulo.");

        thenRepositoryUpdateWasCalled(NULL_EMAIL, VALID_BRAND_CODE, 1);
    }

    @Test
    public void shouldCallRepositoryWhenBrandCodeIsEmpty() {
        whenExecuteUpdateBrandUser(VALID_EMAIL, EMPTY_CODE);

        thenRepositoryUpdateWasCalled(VALID_EMAIL, EMPTY_CODE, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException(VALID_EMAIL, VALID_BRAND_CODE);

        assertThrows(RuntimeException.class,
                () -> whenExecuteUpdateBrandUser(VALID_EMAIL, VALID_BRAND_CODE),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryUpdateWasCalled(VALID_EMAIL, VALID_BRAND_CODE, 1);
    }


    //privadosss
    private void givenRepositoryThrowsRuntimeException(String email, String brandCode) {
        doThrow(new RuntimeException("Simulated DB error"))
                .when(userRepository).updateBrandUser(email, brandCode);
    }

    private void whenExecuteUpdateBrandUser(String userEmail, String brandCode) {
        updateBrandUser.execute(userEmail, brandCode);
    }

    private void thenRepositoryUpdateWasCalled(String expectedEmail, String expectedBrandCode, int times) {
        verify(userRepository, times(times)).updateBrandUser(expectedEmail, expectedBrandCode);
    }
}