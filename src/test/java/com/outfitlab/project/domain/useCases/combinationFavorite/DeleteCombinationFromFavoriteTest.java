package com.outfitlab.project.domain.useCases.combinationFavorite;

import com.outfitlab.project.domain.exceptions.UserCombinationFavoriteNotFoundException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserCombinationFavoriteRepository;
import com.outfitlab.project.domain.model.UserCombinationFavoriteModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeleteCombinationFromFavoriteTest {

    private UserCombinationFavoriteRepository userCombinationFavoriteRepository = mock(UserCombinationFavoriteRepository.class);
    private DeleteCombinationFromFavorite deleteCombinationFromFavorite;

    private final String USER_EMAIL = "user@test.com";
    private final String COMBINATION_URL = "http://url/comb/abc";
    private final String SUCCESS_MESSAGE = "Combinación eliminada de favoritos.";
    private final String NULL_URL = null;
    private final String EMPTY_EMAIL = "";


    @BeforeEach
    void setUp() throws UserCombinationFavoriteNotFoundException, UserNotFoundException {
        deleteCombinationFromFavorite = new DeleteCombinationFromFavorite(userCombinationFavoriteRepository);
        givenFavoriteExists();
        givenDeletionIsSuccessful();
    }


    @Test
    public void shouldDeleteCombinationAndReturnSuccessMessageWhenFavoriteExists() throws Exception {
        String result = whenExecuteDelete(COMBINATION_URL, USER_EMAIL);

        thenResultIsSuccessMessage(result);
        thenFavoriteExistenceWasChecked(1);
        thenDeletionWasCalled(COMBINATION_URL, USER_EMAIL, 1);
    }

    @Test
    public void shouldThrowUserCombinationFavoriteNotFoundExceptionWhenFavoriteDoesNotExist() throws UserNotFoundException {
        givenFavoriteDoesNotExist();

        assertThrows(UserCombinationFavoriteNotFoundException.class,
                () -> whenExecuteDelete(COMBINATION_URL, USER_EMAIL),
                "Debe lanzar NotFoundException si no se encuentra la combinación.");

        thenFavoriteExistenceWasChecked(1);
        thenDeletionWasCalled(COMBINATION_URL, USER_EMAIL, 0);
    }

    @Test
    public void shouldPropagateUserNotFoundExceptionWhenDeletingFavorite() throws UserCombinationFavoriteNotFoundException {
        givenDeletionThrowsUserNotFound();

        assertThrows(UserNotFoundException.class,
                () -> whenExecuteDelete(COMBINATION_URL, USER_EMAIL),
                "Debe propagar UserNotFoundException si el usuario no es válido durante la eliminación.");

        thenFavoriteExistenceWasChecked(1);
        thenDeletionWasCalled(COMBINATION_URL, USER_EMAIL, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenCheckingExistenceFails() {
        givenExistenceCheckThrowsRuntimeException();

        assertThrows(RuntimeException.class,
                () -> whenExecuteDelete(COMBINATION_URL, USER_EMAIL));

        thenFavoriteExistenceWasChecked(1);
        thenDeletionWasCalled(COMBINATION_URL, USER_EMAIL, 0);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenDeletionFails() throws UserNotFoundException {
        givenDeletionThrowsRuntimeException();

        assertThrows(RuntimeException.class,
                () -> whenExecuteDelete(COMBINATION_URL, USER_EMAIL));

        thenFavoriteExistenceWasChecked(1);
        thenDeletionWasCalled(COMBINATION_URL, USER_EMAIL, 1);
    }

    @Test
    public void shouldAttemptDeletionWhenCombinationUrlIsNull() throws Exception {
        whenExecuteDelete(NULL_URL, USER_EMAIL);

        thenFavoriteExistenceWasChecked(1, NULL_URL, USER_EMAIL);
        thenDeletionWasCalled(NULL_URL, USER_EMAIL, 1);
    }

    @Test
    public void shouldAttemptDeletionWhenUserEmailIsEmpty() throws Exception {
        whenExecuteDelete(COMBINATION_URL, EMPTY_EMAIL);

        thenFavoriteExistenceWasChecked(1, COMBINATION_URL, EMPTY_EMAIL);
        thenDeletionWasCalled(COMBINATION_URL, EMPTY_EMAIL, 1);
    }


    //privadosss
    private void givenFavoriteExists() throws UserCombinationFavoriteNotFoundException {
        when(userCombinationFavoriteRepository.findByCombinationUrlAndUserEmail(anyString(), anyString()))
                .thenReturn(mock(UserCombinationFavoriteModel.class));
    }

    private void givenFavoriteDoesNotExist() throws UserCombinationFavoriteNotFoundException {
        doThrow(UserCombinationFavoriteNotFoundException.class)
                .when(userCombinationFavoriteRepository).findByCombinationUrlAndUserEmail(COMBINATION_URL, USER_EMAIL);
    }

    private void givenDeletionIsSuccessful() throws UserNotFoundException {
        doNothing().when(userCombinationFavoriteRepository).deleteFromFavorites(anyString(), anyString());
    }

    private void givenDeletionThrowsUserNotFound() throws UserNotFoundException {
        doThrow(UserNotFoundException.class)
                .when(userCombinationFavoriteRepository).deleteFromFavorites(COMBINATION_URL, USER_EMAIL);
    }

    private void givenExistenceCheckThrowsRuntimeException() {
        doThrow(new RuntimeException("DB error on read")).when(userCombinationFavoriteRepository).findByCombinationUrlAndUserEmail(anyString(), anyString());
    }

    private void givenDeletionThrowsRuntimeException() throws UserNotFoundException {
        doThrow(new RuntimeException("DB error on delete")).when(userCombinationFavoriteRepository).deleteFromFavorites(COMBINATION_URL, USER_EMAIL);
    }

    private String whenExecuteDelete(String combinationUrl, String userEmail) throws UserCombinationFavoriteNotFoundException, UserNotFoundException {
        return deleteCombinationFromFavorite.execute(combinationUrl, userEmail);
    }

    private void thenResultIsSuccessMessage(String result) {
        assertEquals(SUCCESS_MESSAGE, result, "El mensaje de retorno debe ser de eliminación exitosa.");
    }

    private void thenFavoriteExistenceWasChecked(int times) throws UserCombinationFavoriteNotFoundException {
        verify(userCombinationFavoriteRepository, times(times)).findByCombinationUrlAndUserEmail(COMBINATION_URL, USER_EMAIL);
    }

    private void thenFavoriteExistenceWasChecked(int times, String url, String email) throws UserCombinationFavoriteNotFoundException {
        verify(userCombinationFavoriteRepository, times(times)).findByCombinationUrlAndUserEmail(url, email);
    }

    private void thenDeletionWasCalled(String url, String email, int times) throws UserNotFoundException {
        verify(userCombinationFavoriteRepository, times(times)).deleteFromFavorites(url, email);
    }
}