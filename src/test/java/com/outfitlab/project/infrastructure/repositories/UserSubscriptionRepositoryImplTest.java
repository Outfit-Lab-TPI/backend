package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.SubscriptionNotFoundException;
import com.outfitlab.project.domain.model.UserSubscriptionModel;
import com.outfitlab.project.infrastructure.model.SubscriptionEntity;
import com.outfitlab.project.infrastructure.model.UserEntity;
import com.outfitlab.project.infrastructure.model.UserSubscriptionEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.SubscriptionJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserSubscriptionJpaRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UserSubscriptionRepositoryImplTest {

    @Mock
    private UserJpaRepository userJpaRepository;
    @Mock
    private UserSubscriptionJpaRepository userSubscriptionJpaRepository;
    @Mock
    private SubscriptionJpaRepository subscriptionJpaRepository;
    @InjectMocks
    private UserSubscriptionRepositoryImpl repository;

    @Test
    void shouldReturnSubscriptionModelWhenEmailExists() throws SubscriptionNotFoundException {
        String email = "test@mail.com";
        UserSubscriptionEntity entity = givenExistingSubscriptionReturningEntity(email, "BASIC", 10);

        whenFindingSubscriptionByUserEmail(email, entity);

        UserSubscriptionModel result = whenFindByUserEmail(email);

        thenSubscriptionShouldHaveCombinationsUsed(result, 10);
        verify(userSubscriptionJpaRepository).findByUserEmail(email);
    }

    @Test
    void shouldThrowExceptionWhenSubscriptionNotFound() {
        String email = "missing@mail.com";

        whenFindingSubscriptionByUserEmailEmpty(email);

        assertThatThrownBy(() -> whenFindByUserEmail(email))
                .isInstanceOf(SubscriptionNotFoundException.class)
                .hasMessageContaining(email);

        verify(userSubscriptionJpaRepository).findByUserEmail(email);
    }


    @Test
    void shouldSaveSubscriptionCorrectly() {
        String email = "user@mail.com";
        String planCode = "PREMIUM";

        givenExistingUser(email, 5L);
        givenExistingPlan(planCode);
        givenSavedSubscription(3);

        UserSubscriptionModel request = givenSubscriptionModel(email, planCode, 3);
        UserSubscriptionModel result = whenSave(request);

        thenSubscriptionHasCombinations(result, 3);
        verify(userSubscriptionJpaRepository).save(any(UserSubscriptionEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnSave() {
        String email = "fail@mail.com";
        givenNoUser(email);

        UserSubscriptionModel request = givenSubscriptionModel(email, null, 0);

        assertThatThrownBy(() -> whenSave(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void shouldThrowExceptionWhenPlanNotFoundOnSave() {
        String email = "user@mail.com";
        String planCode = "INVALID_PLAN";

        givenExistingUser(email, 1L);
        givenNoPlan(planCode);

        UserSubscriptionModel request = givenSubscriptionModel(email, planCode, 0);

        assertThatThrownBy(() -> whenSave(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Plan no encontrado");
    }

    @Test
    void shouldUpdateSubscriptionCorrectly() {
        String email = "test@mail.com";

        givenExistingSubscription(email, "ESTANDAR", 1);
        givenExistingPlan("ESTANDAR");
        givenExistingPlan("PRO");

        UserSubscriptionModel request = givenSubscriptionModel(email, "PRO", 7);
        UserSubscriptionModel result = whenUpdate(request);

        thenSubscriptionPlanIs(email, "PRO");
        thenSubscriptionHasCombinations(result, 7);
    }

    @Test
    void shouldThrowExceptionWhenSubscriptionNotFoundOnUpdate() {
        String email = "noexiste@mail.com";
        UserSubscriptionModel model = givenSubscriptionModel(email, null, 0);

        givenNoSubscription(email);

        assertThatThrownBy(() -> whenUpdate(model))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Suscripción no encontrada");
    }

    @Test
    void shouldThrowExceptionWhenPlanNotFoundOnUpdate() {
        String email = "test@mail.com";
        String planCode = "TURBO";

        givenExistingSubscription(email, "ANY_PLAN", 0);
        givenNoPlan(planCode);

        UserSubscriptionModel model = givenSubscriptionModel(email, planCode, 0);

        assertThatThrownBy(() -> whenUpdate(model))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Plan no encontrado");
    }

    @Test
    void shouldIncrementCombinationsCounter() {
        String email = "test@mail.com";
        givenExistingUser(email, 1L);

        whenIncrementCounter(email, "combinations");

        thenIncrementCombinationsWasCalled(1L);
    }
    @Test
    void shouldIncrementModelsCounter() {
        String email = "test@mail.com";
        givenExistingUser(email, 1L);

        whenIncrementCounter(email, "3d_models");

        thenIncrementModelsWasCalled(1L);
    }
    @Test
    void givenEmailAnd3dModelsWhenIncrementCounterThenCallsIncrementModels() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userJpaRepository.findByEmail("test@mail.com")).thenReturn(user);

        repository.incrementCounter("test@mail.com", "3d_models");

        verify(userSubscriptionJpaRepository).incrementModelsByUserId(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnCounterIncrement() {
        String email = "noexiste@mail.com";
        givenNoUser(email);

        assertThatThrownBy(() -> whenIncrementCounter(email, "combinations"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void shouldDecrementFavoritesWhenCounterIsFavorites() {
        givenUserWithId("test@mail.com", 2L);

        whenDecrementCounter("test@mail.com", "favorites");

        thenDecrementFavoritesWasCalled(2L);
    }

    @Test
    void shouldDoNothingWhenCounterIsNotFavorite() {
        // No hace falta givenUser porque no lo usa internamente
        whenDecrementCounter("test@mail.com", "models");

        thenDecrementFavoritesWasNeverCalled();
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExistOnDecrement() {
        givenNoUserForEmail("noexiste@mail.com");

        assertThatThrownBy(() -> whenDecrementCounter("noexiste@mail.com", "favorites"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    private void whenFindingSubscriptionByUserEmail(String email, UserSubscriptionEntity entity) {
        when(userSubscriptionJpaRepository.findByUserEmail(email)).thenReturn(Optional.of(entity));
    }

    private void whenFindingSubscriptionByUserEmailEmpty(String email) {
        when(userSubscriptionJpaRepository.findByUserEmail(email)).thenReturn(Optional.empty());
    }

    private UserSubscriptionModel whenFindByUserEmail(String email) throws SubscriptionNotFoundException {
        return repository.findByUserEmail(email);
    }

    private void thenSubscriptionShouldHaveCombinationsUsed(UserSubscriptionModel model, int expected) {
        assertThat(model).isNotNull();
        assertThat(model.getCombinationsUsed()).isEqualTo(expected);
    }

    private UserSubscriptionEntity givenSubscriptionEntity(String email, String planCode, Long userId, int combinations) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail(email);

        SubscriptionEntity plan = new SubscriptionEntity("ESTANDAR", planCode);
        plan.setPlanCode(planCode);

        UserSubscriptionEntity entity = new UserSubscriptionEntity();
        entity.setId(99L);
        entity.setUser(user);
        entity.setSubscription(plan);
        entity.setCombinationsUsed(combinations);

        return entity;
    }

    private void givenExistingSubscription(String email, String planCode, int combinations) {
        UserSubscriptionEntity entity = givenSubscriptionEntity(email, planCode, 10L, combinations);
        when(userSubscriptionJpaRepository.findByUserEmail(email)).thenReturn(Optional.of(entity));
        when(userSubscriptionJpaRepository.save(any(UserSubscriptionEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private UserSubscriptionEntity givenExistingSubscriptionReturningEntity(String email, String planCode, int combinations) {
        UserSubscriptionEntity entity = givenSubscriptionEntity(email, planCode, 10L, combinations);
        when(userSubscriptionJpaRepository.findByUserEmail(email))
                .thenReturn(Optional.of(entity));
        return entity;
    }
    private void givenExistingPlan(String planCode) {
        SubscriptionEntity plan = new SubscriptionEntity("ESTANDAR", planCode);
        plan.setPlanCode(planCode);
        when(subscriptionJpaRepository.findByPlanCode(planCode)).thenReturn(Optional.of(plan));
    }

    private void givenSavedSubscription(int combinations) {
        UserSubscriptionEntity entity = givenSubscriptionEntity("user@mail.com", "PREMIUM", 5L, combinations);
        when(userSubscriptionJpaRepository.save(any(UserSubscriptionEntity.class)))
                .thenReturn(entity);
    }

    private void givenNoSubscription(String email) {
        when(userSubscriptionJpaRepository.findByUserEmail(email))
                .thenReturn(Optional.empty());
    }

    private void givenExistingUser(String email, Long id) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setId(id);
        when(userJpaRepository.findByEmail(email)).thenReturn(user);
    }

    private void givenNoUser(String email) {
        when(userJpaRepository.findByEmail(email)).thenReturn(null);
    }


    private void givenNoPlan(String planCode) {
        when(subscriptionJpaRepository.findByPlanCode(planCode))
                .thenReturn(Optional.empty());
    }

    private UserSubscriptionModel givenSubscriptionModel(String email, String planCode, int used) {
        UserSubscriptionModel model = new UserSubscriptionModel();
        model.setUserEmail(email);
        model.setPlanCode(planCode);
        model.setCombinationsUsed(used);
        return model;
    }

    private UserSubscriptionModel whenSave(UserSubscriptionModel model) {
        return repository.save(model);
    }

    private UserSubscriptionModel whenUpdate(UserSubscriptionModel model) {
        return repository.update(model);
    }

    private void thenSubscriptionHasCombinations(UserSubscriptionModel result, int expected) {
        assertThat(result).isNotNull();
        assertThat(result.getCombinationsUsed()).isEqualTo(expected);
    }

    private void thenSubscriptionPlanIs(String email, String expectedPlan) {
        SubscriptionEntity subscription =
                userSubscriptionJpaRepository.findByUserEmail(email).get().getSubscription();

        assertThat(subscription.getPlanCode()).isEqualTo(expectedPlan);
    }

    private void givenPlanExists(String planCode) {
        SubscriptionEntity plan = new SubscriptionEntity("ESTANDAR", planCode);
        plan.setPlanCode(planCode);
        when(subscriptionJpaRepository.findByPlanCode(planCode)).thenReturn(Optional.of(plan));
    }

    private void whenIncrementCounter(String email, String counterType) {
        repository.incrementCounter(email, counterType);
    }

    private void whenDecrementCounter(String email, String counterType) {
        repository.decrementCounter(email, counterType);
    }

    private void thenIncrementCombinationsWasCalled(Long userId) {
        verify(userSubscriptionJpaRepository).incrementCombinationsByUserId(userId);
    }

    private void thenIncrementFavoritesWasCalled(Long userId) {
        verify(userSubscriptionJpaRepository).incrementFavoritesByUserId(userId);
    }

    private void thenIncrementModelsWasCalled(Long userId) {
        verify(userSubscriptionJpaRepository).incrementModelsByUserId(userId);
    }

    private void thenDecrementFavoritesWasCalled(Long userId) {
        verify(userSubscriptionJpaRepository).decrementFavoritesByUserId(userId);
    }

    private void givenUserWithId(String email, Long id) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setId(id);

        when(userJpaRepository.findByEmail(email)).thenReturn(user);
    }

    private void givenNoUserForEmail(String email) {
        when(userJpaRepository.findByEmail(email)).thenReturn(null);
    }

    private void thenDecrementFavoritesWasNeverCalled() {
        verify(userSubscriptionJpaRepository, never()).decrementFavoritesByUserId(anyLong());
    }
}