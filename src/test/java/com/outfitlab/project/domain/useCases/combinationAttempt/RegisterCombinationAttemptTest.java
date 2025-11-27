package com.outfitlab.project.domain.useCases.combinationAttempt;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.CombinationAttemptModel;
import com.outfitlab.project.domain.model.CombinationModel;
import com.outfitlab.project.domain.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterCombinationAttemptTest {

    private CombinationAttemptRepository attemptRepository = mock(CombinationAttemptRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    private RegisterCombinationAttempt registerCombinationAttempt;

    private static final String VALID_EMAIL = "test@user.com";
    private static final String IMAGE_URL = "http://storage/img/attempt-123.jpg";
    private static final Long EXPECTED_SAVED_ID = 42L;
    private static final Long VALID_COMBINATION_ID = 10L;

    private CombinationModel mockCombination;
    private UserModel mockUser;

    @BeforeEach
    void setUp() throws UserNotFoundException {
        mockCombination = mock(CombinationModel.class);
        mockUser = mock(UserModel.class);
        when(mockCombination.getId()).thenReturn(VALID_COMBINATION_ID);
        registerCombinationAttempt = new RegisterCombinationAttempt(attemptRepository, userRepository);
    }


    @Test
    public void shouldRegisterAttemptAndReturnSavedIdWhenAllDataIsValid() throws UserNotFoundException {
        givenRepositorySavesAttemptAndReturns(EXPECTED_SAVED_ID);
        givenUserExists(VALID_EMAIL, mockUser);

        Long resultId = whenExecuteRegisterAttempt(VALID_EMAIL, mockCombination, IMAGE_URL);

        thenResultMatchesExpectedId(resultId, EXPECTED_SAVED_ID);
        thenRepositoryWasCalledWithCorrectAttempt(mockUser, mockCombination, IMAGE_URL);
        thenUserConsultationWasCalled(VALID_EMAIL, 1);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenUserEmailIsNull() {
        String nullEmail = null;

        assertThrows(IllegalArgumentException.class,
                () -> whenExecuteRegisterAttempt(nullEmail, mockCombination, IMAGE_URL),
                "Debe lanzar IllegalArgumentException si el email es null.");

        thenUserConsultationWasCalled(nullEmail, 0);
        thenRepositorySaveWasNeverCalled();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenCombinationIsNull() throws UserNotFoundException {
        givenUserExists(VALID_EMAIL, mockUser);

        assertThrows(IllegalArgumentException.class,
                () -> whenExecuteRegisterAttempt(VALID_EMAIL, null, IMAGE_URL),
                "Debe lanzar IllegalArgumentException si la combinación es null.");

        thenUserConsultationWasCalled(VALID_EMAIL, 1);
        thenRepositorySaveWasNeverCalled();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenCombinationIdIsNull() throws UserNotFoundException {
        CombinationModel invalidCombination = mock(CombinationModel.class);
        when(invalidCombination.getId()).thenReturn(null);
        givenUserExists(VALID_EMAIL, mockUser);

        assertThrows(IllegalArgumentException.class,
                () -> whenExecuteRegisterAttempt(VALID_EMAIL, invalidCombination, IMAGE_URL),
                "Debe lanzar IllegalArgumentException si el ID de la combinación es null.");

        thenRepositorySaveWasNeverCalled();
    }

    @Test
    public void shouldPropagateUserNotFoundExceptionWhenUserDoesNotExist() {
        givenUserDoesNotExist(VALID_EMAIL);

        assertThrows(UserNotFoundException.class,
                () -> whenExecuteRegisterAttempt(VALID_EMAIL, mockCombination, IMAGE_URL),
                "Debe propagar UserNotFoundException si el usuario no existe.");

        thenUserConsultationWasCalled(VALID_EMAIL, 1);
        thenRepositorySaveWasNeverCalled();
    }

    @Test
    public void shouldRegisterAttemptSuccessfullyWhenImageUrlIsNull() throws UserNotFoundException {
        givenRepositorySavesAttemptAndReturns(EXPECTED_SAVED_ID);
        givenUserExists(VALID_EMAIL, mockUser);
        String nullImageUrl = null;

        Long resultId = whenExecuteRegisterAttempt(VALID_EMAIL, mockCombination, nullImageUrl);

        thenResultMatchesExpectedId(resultId, EXPECTED_SAVED_ID);
        thenRepositoryWasCalledWithCorrectAttempt(mockUser, mockCombination, nullImageUrl);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFailsToSave() throws UserNotFoundException {
        givenRepositoryThrowsRuntimeException();
        givenUserExists(VALID_EMAIL, mockUser);

        assertThrows(RuntimeException.class,
                () -> whenExecuteRegisterAttempt(VALID_EMAIL, mockCombination, IMAGE_URL),
                "Debe propagar RuntimeException si el repositorio falla.");

        thenRepositorySaveWasCalled(1);
    }


    //privadosss
    private void givenUserExists(String email, UserModel user) throws UserNotFoundException {
        when(userRepository.findUserByEmail(email)).thenReturn(user);
    }

    private void givenUserDoesNotExist(String email) {
        when(userRepository.findUserByEmail(email)).thenThrow(UserNotFoundException.class);
    }

    private void givenRepositorySavesAttemptAndReturns(Long id) {
        when(attemptRepository.save(any(CombinationAttemptModel.class))).thenReturn(id);
    }

    private void givenRepositoryThrowsRuntimeException() {
        doThrow(new RuntimeException("DB Connection Error")).when(attemptRepository).save(any(CombinationAttemptModel.class));
    }

    private Long whenExecuteRegisterAttempt(String userEmail, CombinationModel combination, String imageUrl) {
        return registerCombinationAttempt.execute(userEmail, combination, imageUrl);
    }

    private void thenResultMatchesExpectedId(Long actualId, Long expectedId) {
        assertNotNull(actualId, "El ID devuelto no debe ser nulo.");
        assertEquals(expectedId, actualId, "El ID debe coincidir con el ID simulado.");
    }

    private void thenUserConsultationWasCalled(String email, int times) {
        verify(userRepository, times(times)).findUserByEmail(email);
    }

    private void thenRepositorySaveWasCalled(int times) {
        verify(attemptRepository, times(times)).save(any(CombinationAttemptModel.class));
    }

    private void thenRepositorySaveWasNeverCalled() {
        verify(attemptRepository, never()).save(any(CombinationAttemptModel.class));
    }

    private void thenRepositoryWasCalledWithCorrectAttempt(UserModel expectedUser, CombinationModel expectedCombination, String expectedUrl) {
        ArgumentCaptor<CombinationAttemptModel> captor = ArgumentCaptor.forClass(CombinationAttemptModel.class);
        verify(attemptRepository, times(1)).save(captor.capture());

        CombinationAttemptModel capturedAttempt = captor.getValue();

        assertEquals(expectedUser, capturedAttempt.getUser(), "El modelo debe contener el usuario correcto.");
        assertEquals(expectedCombination, capturedAttempt.getCombination(), "El modelo debe contener la combinación correcta.");
        assertEquals(expectedUrl, capturedAttempt.getImageUrl(), "La URL de la imagen debe coincidir.");
    }
}