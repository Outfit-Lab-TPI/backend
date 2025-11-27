package com.outfitlab.project.domain.useCases.combinationFavorite;

import com.outfitlab.project.domain.exceptions.FavoritesException;
import com.outfitlab.project.domain.exceptions.PlanLimitExceededException;
import com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException;
import com.outfitlab.project.domain.exceptions.UserCombinationFavoriteAlreadyExistsException;
import com.outfitlab.project.domain.exceptions.UserCombinationFavoriteNotFoundException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserCombinationFavoriteRepository;
import com.outfitlab.project.domain.model.UserCombinationFavoriteModel;
import com.outfitlab.project.domain.useCases.subscription.CheckUserPlanLimit;
import com.outfitlab.project.domain.useCases.subscription.IncrementUsageCounter;
import com.outfitlab.project.infrastructure.model.UserCombinationFavoriteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddCombinationToFavouriteTest {

    private UserCombinationFavoriteRepository userCombinationFavoriteRepository = mock(UserCombinationFavoriteRepository.class);
    private CheckUserPlanLimit checkUserPlanLimit = mock(CheckUserPlanLimit.class);
    private IncrementUsageCounter incrementUsageCounter = mock(IncrementUsageCounter.class);
    private AddCombinationToFavourite addCombinationToFavourite;

    private final String USER_EMAIL = "user@test.com";
    private final String COMBINATION_URL = "http://url/comb/abc";
    private final String LIMIT_TYPE = "favorites";
    private final String SUCCESS_MESSAGE = "Combinación añadida a favoritos.";

    @BeforeEach
    void setUp() throws UserCombinationFavoriteNotFoundException, FavoritesException, UserNotFoundException {
        addCombinationToFavourite = new AddCombinationToFavourite(
                userCombinationFavoriteRepository,
                checkUserPlanLimit,
                incrementUsageCounter
        );
        givenFavoriteDoesNotExist();
        givenAddToFavoritesIsSuccessful();
    }


    @Test
    public void shouldAddCombinationAndIncrementCounterWhenChecksPass() throws Exception {
        String result = whenExecuteAddCombination(COMBINATION_URL, USER_EMAIL);

        thenResultIsSuccessMessage(result);
        thenPlanLimitWasChecked(1);
        thenFavoriteExistenceWasChecked(1);
        thenAddToFavoritesWasCalled(COMBINATION_URL, USER_EMAIL, 1);
        thenUsageCounterWasIncremented(1);
    }

    @Test
    public void shouldThrowPlanLimitExceededExceptionWhenLimitIsReached() throws Exception {
        doThrow(PlanLimitExceededException.class).when(checkUserPlanLimit).execute(USER_EMAIL, LIMIT_TYPE);

        assertThrows(PlanLimitExceededException.class,
                () -> whenExecuteAddCombination(COMBINATION_URL, USER_EMAIL));

        thenPlanLimitWasChecked(1);
        thenFavoriteExistenceWasChecked(0);
        thenAddToFavoritesWasCalled(COMBINATION_URL, USER_EMAIL, 0);
        thenUsageCounterWasIncremented(0);
    }

    @Test
    public void shouldThrowUserCombinationFavoriteAlreadyExistsExceptionWhenAlreadyExists() throws Exception {
        givenFavoriteAlreadyExists();

        assertThrows(UserCombinationFavoriteAlreadyExistsException.class,
                () -> whenExecuteAddCombination(COMBINATION_URL, USER_EMAIL));

        thenPlanLimitWasChecked(1);
        thenFavoriteExistenceWasChecked(1);
        thenAddToFavoritesWasCalled(COMBINATION_URL, USER_EMAIL, 0);
        thenUsageCounterWasIncremented(0);
    }

    @Test
    public void shouldThrowFavoritesExceptionWhenAddToFavoritesFails() throws Exception {
        givenAddToFavoritesFailsWithNull();

        assertThrows(FavoritesException.class,
                () -> whenExecuteAddCombination(COMBINATION_URL, USER_EMAIL));

        thenPlanLimitWasChecked(1);
        thenFavoriteExistenceWasChecked(1);
        thenAddToFavoritesWasCalled(COMBINATION_URL, USER_EMAIL, 1);
        thenUsageCounterWasIncremented(0);
    }

    @Test
    public void shouldPropagateSubscriptionNotFoundException() throws Exception {
        doThrow(SubscriptionNotFoundException.class).when(checkUserPlanLimit).execute(USER_EMAIL, LIMIT_TYPE);

        assertThrows(SubscriptionNotFoundException.class,
                () -> whenExecuteAddCombination(COMBINATION_URL, USER_EMAIL));

        thenPlanLimitWasChecked(1);
        thenFavoriteExistenceWasChecked(0);
        thenUsageCounterWasIncremented(0);
    }

    @Test
    public void shouldPropagateUserNotFoundExceptionWhenAddingToFavorites() throws Exception {
        givenAddToFavoritesThrowsUserNotFound();

        assertThrows(UserNotFoundException.class,
                () -> whenExecuteAddCombination(COMBINATION_URL, USER_EMAIL));

        thenPlanLimitWasChecked(1);
        thenFavoriteExistenceWasChecked(1);
        thenAddToFavoritesWasCalled(COMBINATION_URL, USER_EMAIL, 1);
        thenUsageCounterWasIncremented(0);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenCheckingIfFavoriteExistsFails() throws Exception {
        givenFavoriteExistenceThrowsRuntimeException();

        assertThrows(RuntimeException.class,
                () -> whenExecuteAddCombination(COMBINATION_URL, USER_EMAIL));

        thenPlanLimitWasChecked(1);
        thenFavoriteExistenceWasChecked(1);
        thenAddToFavoritesWasCalled(COMBINATION_URL, USER_EMAIL, 0);
        thenUsageCounterWasIncremented(0);
    }


    //privadoss
    private void givenFavoriteDoesNotExist() throws UserCombinationFavoriteNotFoundException {
        doThrow(UserCombinationFavoriteNotFoundException.class)
                .when(userCombinationFavoriteRepository).findByCombinationUrlAndUserEmail(COMBINATION_URL, USER_EMAIL);
    }

    private void givenFavoriteAlreadyExists() throws UserCombinationFavoriteNotFoundException {
        doReturn(mock(UserCombinationFavoriteModel.class))
                .when(userCombinationFavoriteRepository).findByCombinationUrlAndUserEmail(COMBINATION_URL, USER_EMAIL);
    }

    private void givenFavoriteExistenceThrowsRuntimeException() throws UserCombinationFavoriteNotFoundException {
        doThrow(new RuntimeException("DB Connection Error during check"))
                .when(userCombinationFavoriteRepository).findByCombinationUrlAndUserEmail(COMBINATION_URL, USER_EMAIL);
    }

    private void givenAddToFavoritesIsSuccessful() throws UserNotFoundException {
        UserCombinationFavoriteEntity successEntity = mock(UserCombinationFavoriteEntity.class);
        when(userCombinationFavoriteRepository.addToFavorite(anyString(), anyString())).thenReturn(successEntity);
    }

    private void givenAddToFavoritesFailsWithNull() {
        when(userCombinationFavoriteRepository.addToFavorite(anyString(), anyString())).thenReturn(null);
    }

    private void givenAddToFavoritesThrowsUserNotFound() throws UserNotFoundException {
        doThrow(UserNotFoundException.class)
                .when(userCombinationFavoriteRepository).addToFavorite(COMBINATION_URL, USER_EMAIL);
    }

    private String whenExecuteAddCombination(String combinationUrl, String userEmail) throws Exception {
        return addCombinationToFavourite.execute(combinationUrl, userEmail);
    }

    private void thenResultIsSuccessMessage(String result) {
        assertEquals(SUCCESS_MESSAGE, result, "El mensaje de retorno debe ser el de éxito.");
    }

    private void thenPlanLimitWasChecked(int times) throws Exception {
        verify(checkUserPlanLimit, times(times)).execute(USER_EMAIL, LIMIT_TYPE);
    }

    private void thenFavoriteExistenceWasChecked(int times) throws UserCombinationFavoriteNotFoundException {
        verify(userCombinationFavoriteRepository, times(times)).findByCombinationUrlAndUserEmail(COMBINATION_URL, USER_EMAIL);
    }

    private void thenAddToFavoritesWasCalled(String url, String email, int times) throws FavoritesException, UserNotFoundException {
        verify(userCombinationFavoriteRepository, times(times)).addToFavorite(url, email);
    }

    private void thenUsageCounterWasIncremented(int times) {
        verify(incrementUsageCounter, times(times)).execute(USER_EMAIL, LIMIT_TYPE);
    }
}