package com.outfitlab.project.domain.useCases.subscription;

import com.outfitlab.project.domain.exceptions.PlanLimitExceededException;
import com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import com.outfitlab.project.domain.model.UserSubscriptionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CheckUserPlanLimitTest {

    private UserSubscriptionRepository userSubscriptionRepository = mock(UserSubscriptionRepository.class);

    private CheckUserPlanLimit checkUserPlanLimit;

    private final String USER_EMAIL = "user@plan.com";
    private final String LIMIT_COMBINATIONS = "combinations";
    private final String LIMIT_FAVORITES = "favorites";
    private final String LIMIT_3D_MODELS = "3d_models";
    private final String LIMIT_DOWNLOADS = "downloads";
    private final int MAX_LIMIT = 10;
    private final int CURRENT_USAGE_SAFE = MAX_LIMIT - 1;
    private final int CURRENT_USAGE_EXCEEDED = MAX_LIMIT + 1;

    @BeforeEach
    void setUp() {
        checkUserPlanLimit = new CheckUserPlanLimit(userSubscriptionRepository);
    }


    @Test
    public void shouldNotThrowExceptionWhenCombinationsLimitIsNotReached() throws PlanLimitExceededException, SubscriptionNotFoundException {
        givenSubscriptionExists(CURRENT_USAGE_SAFE, MAX_LIMIT, 0, MAX_LIMIT, 0, MAX_LIMIT);

        whenExecuteCheckLimit(LIMIT_COMBINATIONS);

        thenRepositoryWasCalledOnce(USER_EMAIL);
    }

    @Test
    public void shouldNotThrowExceptionWhenFavoritesLimitIsNotReached() throws PlanLimitExceededException, SubscriptionNotFoundException {
        givenSubscriptionExists(0, MAX_LIMIT, CURRENT_USAGE_SAFE, MAX_LIMIT, 0, MAX_LIMIT);

        whenExecuteCheckLimit(LIMIT_FAVORITES);

        thenRepositoryWasCalledOnce(USER_EMAIL);
    }

    @Test
    public void shouldNotThrowExceptionWhenCombinationsLimitIsUnlimitedNull() throws PlanLimitExceededException, SubscriptionNotFoundException {
        givenSubscriptionExists(CURRENT_USAGE_EXCEEDED, null, 0, MAX_LIMIT, 0, MAX_LIMIT);
        whenExecuteCheckLimit(LIMIT_COMBINATIONS);
    }

    @Test
    public void shouldNotThrowExceptionWhenDownloadsLimitIsUnlimitedNegativeOne() throws PlanLimitExceededException, SubscriptionNotFoundException {
        givenSubscriptionExists(0, MAX_LIMIT, 0, MAX_LIMIT, CURRENT_USAGE_EXCEEDED, -1);
        whenExecuteCheckLimit(LIMIT_DOWNLOADS);
    }

    @Test
    public void shouldThrowPlanLimitExceededExceptionWhenModelsLimitIsExceeded() throws SubscriptionNotFoundException {
        givenSubscriptionExists(0, MAX_LIMIT, CURRENT_USAGE_EXCEEDED, MAX_LIMIT, 0, MAX_LIMIT);

        assertThrows(PlanLimitExceededException.class,
                () -> whenExecuteCheckLimit(LIMIT_3D_MODELS),
                "Se esperaba PlanLimitExceededException al exceder modelos 3D.");

        thenRepositoryWasCalledOnce(USER_EMAIL);
    }

    @Test
    public void shouldThrowPlanLimitExceededExceptionWhenDownloadsLimitIsExactlyReached() throws SubscriptionNotFoundException {
        givenSubscriptionExists(0, MAX_LIMIT, 0, MAX_LIMIT, MAX_LIMIT, MAX_LIMIT);

        assertThrows(PlanLimitExceededException.class,
                () -> whenExecuteCheckLimit(LIMIT_DOWNLOADS),
                "Se esperaba PlanLimitExceededException al alcanzar el límite exacto.");

        thenRepositoryWasCalledOnce(USER_EMAIL);
    }

    @Test
    public void shouldThrowSubscriptionNotFoundExceptionWhenSubscriptionDoesNotExist() throws SubscriptionNotFoundException {
        givenRepositoryThrowsSubscriptionNotFound();

        assertThrows(SubscriptionNotFoundException.class,
                () -> whenExecuteCheckLimit(LIMIT_FAVORITES),
                "Se esperaba SubscriptionNotFoundException cuando no hay suscripción.");

        thenRepositoryWasCalledOnce(USER_EMAIL);
    }


    //privadoss
    private void givenSubscriptionExists(int combinationsUsed, Integer maxCombinations,
                                         int favoritesCount, Integer maxFavorites,
                                         int downloadsCount, Integer maxDownloads) throws SubscriptionNotFoundException {

        UserSubscriptionModel mockSubscription = mock(UserSubscriptionModel.class);

        when(mockSubscription.getCombinationsUsed()).thenReturn(combinationsUsed);
        when(mockSubscription.getMaxCombinations()).thenReturn(maxCombinations);

        when(mockSubscription.getFavoritesCount()).thenReturn(favoritesCount);
        when(mockSubscription.getMaxFavorites()).thenReturn(maxFavorites);

        when(mockSubscription.getModelsGenerated()).thenReturn(favoritesCount);
        when(mockSubscription.getMaxModels()).thenReturn(maxFavorites);

        when(mockSubscription.getDownloadsCount()).thenReturn(downloadsCount);
        when(mockSubscription.getMaxDownloads()).thenReturn(maxDownloads);

        when(userSubscriptionRepository.findByUserEmail(USER_EMAIL)).thenReturn(mockSubscription);
    }

    private void givenRepositoryThrowsSubscriptionNotFound() throws SubscriptionNotFoundException {
        when(userSubscriptionRepository.findByUserEmail(USER_EMAIL)).thenThrow(SubscriptionNotFoundException.class);
    }

    private void whenExecuteCheckLimit(String limitType) throws PlanLimitExceededException, SubscriptionNotFoundException {
        checkUserPlanLimit.execute(USER_EMAIL, limitType);
    }

    private void thenRepositoryWasCalledOnce(String userEmail) throws SubscriptionNotFoundException {
        verify(userSubscriptionRepository, times(1)).findByUserEmail(userEmail);
    }
}