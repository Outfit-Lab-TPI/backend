package com.outfitlab.project.domain.useCases.subscription;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.SubscriptionRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.SubscriptionModel;
import com.outfitlab.project.domain.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetAllSubscriptionTest {

    private SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private GetAllSubscription getAllSubscription;

    private final String USER_EMAIL = "normal@user.com";
    private final String BRAND_EMAIL = "brand@user.com";
    private final String PLAN_TYPE_USER = "USER";
    private final String PLAN_TYPE_BRAND = "BRAND";

    @BeforeEach
    void setUp() {
        getAllSubscription = new GetAllSubscription(subscriptionRepository, userRepository);
    }


    @Test
    public void shouldReturnUserPlansWhenEmailIsNull() {
        String nullEmail = null;
        givenRepositoryReturnsPlans(PLAN_TYPE_USER, 3);

        List<SubscriptionModel> result = whenExecuteGetAllSubscription(nullEmail);

        thenResultContainsPlans(result, PLAN_TYPE_USER, 3);
        thenUserConsultationWasNeverCalled();
    }

    @Test
    public void shouldReturnUserPlansWhenEmailIsEmpty() {
        String emptyEmail = "";
        givenRepositoryReturnsPlans(PLAN_TYPE_USER, 5);

        List<SubscriptionModel> result = whenExecuteGetAllSubscription(emptyEmail);

        thenResultContainsPlans(result, PLAN_TYPE_USER, 5);
        thenUserConsultationWasNeverCalled();
    }

    @Test
    public void shouldReturnUserPlansWhenUserIsFoundAndIsNotBrand() throws UserNotFoundException {
        givenUserExists(USER_EMAIL, false);
        givenRepositoryReturnsPlans(PLAN_TYPE_USER, 2);

        List<SubscriptionModel> result = whenExecuteGetAllSubscription(USER_EMAIL);

        thenResultContainsPlans(result, PLAN_TYPE_USER, 2);
        thenUserConsultationWasCalled(USER_EMAIL);
    }

    @Test
    public void shouldReturnBrandPlansWhenUserIsFoundAndIsBrand() throws UserNotFoundException {
        givenUserExists(BRAND_EMAIL, true);
        givenRepositoryReturnsPlans(PLAN_TYPE_BRAND, 4);

        List<SubscriptionModel> result = whenExecuteGetAllSubscription(BRAND_EMAIL);

        thenResultContainsPlans(result, PLAN_TYPE_BRAND, 4);
        thenUserConsultationWasCalled(BRAND_EMAIL);
    }

    @Test
    public void shouldReturnUserPlansWhenUserIsNotFound() throws UserNotFoundException {
        givenUserDoesNotExist(USER_EMAIL);
        givenRepositoryReturnsPlans(PLAN_TYPE_USER, 1);

        List<SubscriptionModel> result = whenExecuteGetAllSubscription(USER_EMAIL);

        thenResultContainsPlans(result, PLAN_TYPE_USER, 1);
        thenUserConsultationWasCalled(USER_EMAIL);
    }


    //privadoss
    private void givenUserExists(String email, boolean isBrand) throws UserNotFoundException {
        UserModel user = mock(UserModel.class);

        BrandModel brandObjectMock = isBrand ? mock(BrandModel.class) : null;

        when(user.getBrand()).thenReturn(brandObjectMock);
        when(userRepository.findUserByEmail(email)).thenReturn(user);
    }

    private void givenUserDoesNotExist(String email) throws UserNotFoundException {
        when(userRepository.findUserByEmail(email)).thenThrow(new UserNotFoundException("Usuario no encontrado"));
    }

    private void givenRepositoryReturnsPlans(String planType, int count) {
        List<SubscriptionModel> mockPlans = Collections.nCopies(count, new SubscriptionModel());
        when(subscriptionRepository.findByPlanType(planType)).thenReturn(mockPlans);
    }

    private List<SubscriptionModel> whenExecuteGetAllSubscription(String userEmail) {
        return getAllSubscription.execute(userEmail);
    }

    private void thenResultContainsPlans(List<SubscriptionModel> result, String expectedPlanType, int expectedCount) {
        assertNotNull(result, "El resultado no debe ser nulo.");
        assertEquals(expectedCount, result.size(), "El número de planes devuelto debe ser el esperado.");

        verify(subscriptionRepository, times(1)).findByPlanType(expectedPlanType);
    }

    private void thenUserConsultationWasCalled(String email) throws UserNotFoundException {
        verify(userRepository, times(1)).findUserByEmail(email);
    }

    private void thenUserConsultationWasNeverCalled() {
        verify(userRepository, never()).findUserByEmail(anyString());
    }
}