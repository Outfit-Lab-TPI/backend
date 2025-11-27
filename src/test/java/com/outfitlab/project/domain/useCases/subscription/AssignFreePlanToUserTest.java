package com.outfitlab.project.domain.useCases.subscription;

import com.outfitlab.project.domain.interfaces.repositories.SubscriptionRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserSubscriptionRepository;
import com.outfitlab.project.domain.model.SubscriptionModel;
import com.outfitlab.project.domain.model.UserSubscriptionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssignFreePlanToUserTest {

    private UserSubscriptionRepository userSubscriptionRepository = mock(UserSubscriptionRepository.class);
    private SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);

    private AssignFreePlanToUser assignFreePlanToUser;

    private final String USER_EMAIL = "new_user@example.com";
    private final String USER_FREE_PLAN_CODE = "user-free-monthly";
    private final String BRAND_FREE_PLAN_CODE = "brand-free-monthly";

    @BeforeEach
    void setUp() {
        assignFreePlanToUser = new AssignFreePlanToUser(userSubscriptionRepository, subscriptionRepository);
    }


    @Test
    public void shouldAssignUserFreePlanWhenUserIsNotBrand() {
        boolean isBrand = false;
        SubscriptionModel mockFreePlan = givenSubscriptionPlanExists(USER_FREE_PLAN_CODE, "10", "5", "Ilimitado");

        whenExecuteAssignFreePlanToUser(USER_EMAIL, isBrand);

        thenRepositoryWasCalledWithCorrectPlan(USER_FREE_PLAN_CODE, 10, 5, null);
    }

    @Test
    public void shouldAssignBrandFreePlanWhenUserIsBrand() {
        boolean isBrand = true;
        SubscriptionModel mockFreePlan = givenSubscriptionPlanExists(BRAND_FREE_PLAN_CODE, "20", "20", "5");

        whenExecuteAssignFreePlanToUser(USER_EMAIL, isBrand);

        thenRepositoryWasCalledWithCorrectPlan(BRAND_FREE_PLAN_CODE, 20, 20, 5);
    }

    @Test
    public void shouldHandleNullOrEmptyFeaturesGracefully() {
        boolean isBrand = false;
        SubscriptionModel mockFreePlan = givenSubscriptionPlanExists(USER_FREE_PLAN_CODE, null, "", "1");

        whenExecuteAssignFreePlanToUser(USER_EMAIL, isBrand);

        thenRepositoryWasCalledWithCorrectPlan(USER_FREE_PLAN_CODE, null, null, 1);
    }


    //privadoss
    private SubscriptionModel givenSubscriptionPlanExists(String planCode, String feature1, String feature2, String feature3) {
        SubscriptionModel mockPlan = mock(SubscriptionModel.class);

        when(subscriptionRepository.getByPlanCode(planCode)).thenReturn(mockPlan);

        when(mockPlan.getPlanCode()).thenReturn(planCode);
        when(mockPlan.getFeature1()).thenReturn(feature2); //combinaciones
        when(mockPlan.getFeature2()).thenReturn(feature1); //favoritos
        when(mockPlan.getFeature3()).thenReturn(feature3); //3d

        return mockPlan;
    }

    private void whenExecuteAssignFreePlanToUser(String userEmail, boolean isBrand) {
        assignFreePlanToUser.execute(userEmail, isBrand);
    }

    private void thenRepositoryWasCalledWithCorrectPlan(String expectedPlanCode, Integer expectedMaxCombinations, Integer expectedMaxFavorites, Integer expectedMaxModels) {
        verify(subscriptionRepository, times(1)).getByPlanCode(expectedPlanCode);

        ArgumentCaptor<UserSubscriptionModel> argumentCaptor = ArgumentCaptor.forClass(UserSubscriptionModel.class);
        verify(userSubscriptionRepository, times(1)).save(argumentCaptor.capture());

        UserSubscriptionModel capturedSubscription = argumentCaptor.getValue();

        assertEquals(USER_EMAIL, capturedSubscription.getUserEmail(), "El email del usuario debe coincidir.");
        assertEquals(expectedPlanCode, capturedSubscription.getPlanCode(), "El código de plan asignado debe ser el esperado.");
        assertEquals("ACTIVE", capturedSubscription.getStatus(), "El estado de la suscripción debe ser ACTIVO.");

        assertEquals(expectedMaxCombinations, capturedSubscription.getMaxCombinations(), "El límite de combinaciones debe ser el esperado.");
        assertEquals(expectedMaxFavorites, capturedSubscription.getMaxFavorites(), "El límite de favoritos debe ser el esperado.");
        assertEquals(expectedMaxModels, capturedSubscription.getMaxModels(), "El límite de modelos generados debe ser el esperado (null para Ilimitado).");

        assertNotNull(capturedSubscription.getSubscriptionStart(), "La fecha de inicio de suscripción no debe ser nula.");
        assertTrue(capturedSubscription.getSubscriptionStart().isBefore(LocalDateTime.now().plusSeconds(1)), "La fecha de inicio debe ser reciente.");
    }
}