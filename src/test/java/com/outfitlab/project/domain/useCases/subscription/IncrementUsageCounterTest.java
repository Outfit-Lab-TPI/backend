package com.outfitlab.project.domain.useCases.subscription;

import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class IncrementUsageCounterTest {

    private UserSubscriptionRepository userSubscriptionRepository = mock(UserSubscriptionRepository.class);
    private IncrementUsageCounter incrementUsageCounter;

    private final String USER_EMAIL = "test@user.com";
    private final String COUNTER_COMBINATIONS = "combinations";
    private final String COUNTER_MODELS = "3d_models";
    private final String COUNTER_FAVORITES = "favorites";

    @BeforeEach
    void setUp() {
        incrementUsageCounter = new IncrementUsageCounter(userSubscriptionRepository);
    }


    @Test
    public void shouldCallRepositoryToIncrementCombinationsCounter() {
        whenExecuteIncrementCounter(USER_EMAIL, COUNTER_COMBINATIONS);

        thenRepositoryIncrementCounterWasCalled(USER_EMAIL, COUNTER_COMBINATIONS);
    }

    @Test
    public void shouldCallRepositoryToIncrementModelsCounter() {
        whenExecuteIncrementCounter(USER_EMAIL, COUNTER_MODELS);

        thenRepositoryIncrementCounterWasCalled(USER_EMAIL, COUNTER_MODELS);
    }

    @Test
    public void shouldCallRepositoryToIncrementFavoritesCounter() {
        whenExecuteIncrementCounter(USER_EMAIL, COUNTER_FAVORITES);

        thenRepositoryIncrementCounterWasCalled(USER_EMAIL, COUNTER_FAVORITES);
    }


    //privadoss
    private void whenExecuteIncrementCounter(String userEmail, String counterType) {
        incrementUsageCounter.execute(userEmail, counterType);
    }

    private void thenRepositoryIncrementCounterWasCalled(String userEmail, String counterType) {
        verify(userSubscriptionRepository, times(1)).incrementCounter(userEmail, counterType);
    }
}