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
    void givenEmailWhenFindByUserEmailThenReturnSubscriptionModel() throws SubscriptionNotFoundException {
        UserEntity user = new UserEntity();
        user.setEmail("test@mail.com");

        SubscriptionEntity plan = new SubscriptionEntity();
        plan.setPlanCode("BASIC");

        UserSubscriptionEntity entity = new UserSubscriptionEntity();
        entity.setUser(user);
        entity.setSubscription(plan);
        entity.setCombinationsUsed(10);

        when(userSubscriptionJpaRepository.findByUserEmail("test@mail.com"))
                .thenReturn(Optional.of(entity));

        UserSubscriptionModel result = repository.findByUserEmail("test@mail.com");

        assertThat(result).isNotNull();
        assertThat(result.getCombinationsUsed()).isEqualTo(10);
        verify(userSubscriptionJpaRepository).findByUserEmail("test@mail.com");
    }

    @Test
    void givenEmailWhenSubscriptionNotFoundThenThrowException() {
        when(userSubscriptionJpaRepository.findByUserEmail("missing@mail.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findByUserEmail("missing@mail.com"))
                .isInstanceOf(SubscriptionNotFoundException.class)
                .hasMessageContaining("missing@mail.com");

        verify(userSubscriptionJpaRepository).findByUserEmail("missing@mail.com");
    }

    @Test
    void givenSubscriptionModelWhenSaveThenPersistAndReturnModel() {
        UserEntity user = new UserEntity();
        user.setEmail("user@mail.com");
        user.setId(5L);

        SubscriptionEntity plan = new SubscriptionEntity();
        plan.setPlanCode("PREMIUM");

        when(userJpaRepository.findByEmail("user@mail.com")).thenReturn(user);
        when(subscriptionJpaRepository.findByPlanCode("PREMIUM")).thenReturn(Optional.of(plan));

        UserSubscriptionEntity savedEntity = new UserSubscriptionEntity();
        savedEntity.setUser(user);
        savedEntity.setSubscription(plan);
        savedEntity.setCombinationsUsed(3);

        when(userSubscriptionJpaRepository.save(any(UserSubscriptionEntity.class))).thenReturn(savedEntity);

        UserSubscriptionModel model = new UserSubscriptionModel();
        model.setUserEmail("user@mail.com");
        model.setPlanCode("PREMIUM");
        model.setCombinationsUsed(3);

        UserSubscriptionModel result = repository.save(model);

        assertThat(result).isNotNull();
        assertThat(result.getCombinationsUsed()).isEqualTo(3);
        verify(userSubscriptionJpaRepository).save(any(UserSubscriptionEntity.class));
    }

    @Test
    void givenInvalidUserWhenSaveThenThrowException() {
        UserSubscriptionModel model = new UserSubscriptionModel();
        model.setUserEmail("fail@mail.com");

        when(userJpaRepository.findByEmail("fail@mail.com")).thenReturn(null);

        assertThatThrownBy(() -> repository.save(model))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void givenInvalidPlanWhenSaveThenThrowException() {
        UserSubscriptionModel model = new UserSubscriptionModel();
        model.setUserEmail("user@mail.com");
        model.setPlanCode("PLAN A");

        when(userJpaRepository.findByEmail("user@mail.com")).thenReturn(new UserEntity());
        when(subscriptionJpaRepository.findByPlanCode("PLAN A")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.save(model))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Plan no encontrado");
    }

    @Test
    void givenSubscriptionModelWhenUpdateThenUpdateAndReturnModel() {
        SubscriptionEntity oldPlan = new SubscriptionEntity();
        oldPlan.setPlanCode("ESTANDAR");

        UserSubscriptionEntity existingUserSubs = new UserSubscriptionEntity();
        existingUserSubs.setCombinationsUsed(1);
        existingUserSubs.setUser(new UserEntity("test@mail.com"));
        existingUserSubs.setSubscription(oldPlan);

        SubscriptionEntity newPlan = new SubscriptionEntity();
        newPlan.setPlanCode("PRO");

        when(userSubscriptionJpaRepository.findByUserEmail("test@mail.com"))
                .thenReturn(Optional.of(existingUserSubs));
        when(subscriptionJpaRepository.findByPlanCode("ESTANDAR"))
                .thenReturn(Optional.of(oldPlan));
        when(subscriptionJpaRepository.findByPlanCode("PRO"))
                .thenReturn(Optional.of(newPlan));
        when(userSubscriptionJpaRepository.save(existingUserSubs)).thenReturn(existingUserSubs);

        UserSubscriptionModel model = new UserSubscriptionModel();
        model.setUserEmail("test@mail.com");
        model.setPlanCode("PRO");
        model.setCombinationsUsed(7);

        UserSubscriptionModel result = repository.update(model);

        assertThat(existingUserSubs.getSubscription()).isEqualTo(newPlan);
        assertThat(existingUserSubs.getCombinationsUsed()).isEqualTo(7);
        assertThat(result).isNotNull();
    }

    @Test
    void givenNonExistingSubscriptionWhenUpdateThenThrowException() {
        UserSubscriptionModel model = new UserSubscriptionModel();
        model.setUserEmail("noexiste@mail.com");

        when(userSubscriptionJpaRepository.findByUserEmail("noexiste@mail.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.update(model))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Suscripción no encontrada");
    }

    @Test
    void givenInvalidPlanWhenUpdateThenThrowException() {
        UserSubscriptionModel model = new UserSubscriptionModel();
        model.setUserEmail("test@mail.com");
        model.setPlanCode("TURBO");

        when(userSubscriptionJpaRepository.findByUserEmail("test@mail.com"))
                .thenReturn(Optional.of(new UserSubscriptionEntity()));
        when(subscriptionJpaRepository.findByPlanCode("TURBO"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.update(model))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Plan no encontrado");
    }

    @Test
    void givenEmailAndCombinationsWhenIncrementCounterThenCallsIncrementCombinations() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userJpaRepository.findByEmail("test@mail.com")).thenReturn(user);

        repository.incrementCounter("test@mail.com", "combinations");

        verify(userSubscriptionJpaRepository).incrementCombinationsByUserId(1L);
    }

    @Test
    void givenEmailAndFavoritesWhenIncrementCounterThenCallsIncrementFavorites() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userJpaRepository.findByEmail("test@mail.com")).thenReturn(user);

        repository.incrementCounter("test@mail.com", "favorites");

        verify(userSubscriptionJpaRepository).incrementFavoritesByUserId(1L);
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
    void givenNonexistentUserWhenIncrementCounterThenThrowException() {
        when(userJpaRepository.findByEmail("noexiste@mail.com")).thenReturn(null);

        assertThatThrownBy(() -> repository.incrementCounter("noexiste@mail.com", "combinations"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void givenEmailAndFavoritesWhenDecrementCounterThenCallsDecrementFavorites() {
        UserEntity user = new UserEntity();
        user.setId(2L);

        when(userJpaRepository.findByEmail("test@mail.com")).thenReturn(user);

        repository.decrementCounter("test@mail.com", "favorites");

        verify(userSubscriptionJpaRepository).decrementFavoritesByUserId(2L);
    }

    @Test
    void givenNonFavoriteCounterWhenDecrementCounterThenDoNothing() {
        repository.decrementCounter("test@mail.com", "models");

        verify(userSubscriptionJpaRepository, never()).decrementFavoritesByUserId(anyLong());
    }

    @Test
    void givenNonexistentUserWhenDecrementCounterThenThrowException() {
        when(userJpaRepository.findByEmail("no@mail.com")).thenReturn(null);

        assertThatThrownBy(() -> repository.decrementCounter("no@mail.com", "favorites"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

}