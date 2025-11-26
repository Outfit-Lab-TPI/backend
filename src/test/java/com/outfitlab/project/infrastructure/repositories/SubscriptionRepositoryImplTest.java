package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.model.SubscriptionModel;
import com.outfitlab.project.infrastructure.model.SubscriptionEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.SubscriptionJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class SubscriptionRepositoryImplTest {
    @Mock
    private SubscriptionJpaRepository jpaRepository;

    @InjectMocks
    private SubscriptionRepositoryImpl repository;

    @Test
    public void shouldGetAllSubscriptions() {
        // given
        List<SubscriptionEntity> entities = givenSubscriptionEntities();
        givenJpaFindAllReturns(entities);

        // when
        List<SubscriptionModel> result = whenGetAllSubscriptions();

        // then
        thenResultShouldMatchEntities(result, entities);
    }

    @Test
    public void shouldGetSubscriptionByPlanCode() {
        // given
        String planCode = "PREMIUM";
        SubscriptionEntity entity = givenSubscriptionEntity(planCode);
        givenJpaFindByPlanCodeReturns(planCode, entity);

        // when
        SubscriptionModel result = whenGetByPlanCode(planCode);

        // then
        thenModelShouldMatchEntity(result, entity);
    }

    @Test
    public void shouldThrowExceptionWhenPlanCodeNotFound() {
        // given
        String planCode = "INVALID";
        givenJpaFindByPlanCodeReturnsEmpty(planCode);

        // then
        thenShouldThrowPlanNotFound(planCode);
    }
    @Test
    public void shouldFindSubscriptionsByPlanType() {
        // given
        String planType = "MONTHLY";
        List<SubscriptionEntity> entities = givenSubscriptionEntities();
        givenJpaFindByPlanTypeReturns(planType, entities);

        List<SubscriptionModel> result = whenFindByPlanType(planType);

        thenResultShouldMatchEntities(result, entities);
    }

    private List<SubscriptionEntity> givenSubscriptionEntities() {
        return List.of(
                new SubscriptionEntity("ESTANDAR", "ABC123"),
                new SubscriptionEntity("PRO", "DEF456")
        );
    }

    private SubscriptionEntity givenSubscriptionEntity(String planCode) {
        return new SubscriptionEntity(planCode, "ABC123");
    }

    private void givenJpaFindAllReturns(List<SubscriptionEntity> entities) {
        when(jpaRepository.findAll()).thenReturn(entities);
    }

    private void givenJpaFindByPlanCodeReturns(String planCode, SubscriptionEntity entity) {
        when(jpaRepository.findByPlanCode(planCode)).thenReturn(Optional.of(entity));
    }

    private void givenJpaFindByPlanCodeReturnsEmpty(String planCode) {
        when(jpaRepository.findByPlanCode(planCode)).thenReturn(Optional.empty());
    }

    private void givenJpaFindByPlanTypeReturns(String planType, List<SubscriptionEntity> entities) {
        when(jpaRepository.findByPlanType(planType)).thenReturn(entities);
    }

    private List<SubscriptionModel> whenGetAllSubscriptions() {
        return repository.getAllSubscriptions();
    }

    private SubscriptionModel whenGetByPlanCode(String planCode) {
        return repository.getByPlanCode(planCode);
    }

    private List<SubscriptionModel> whenFindByPlanType(String planType) {
        return repository.findByPlanType(planType);
    }
    private void thenShouldThrowPlanNotFound(String planCode) {
        assertThatThrownBy(() -> whenGetByPlanCode(planCode))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Plan no encontrado: " + planCode);
    }

    private void thenResultShouldMatchEntities(List<SubscriptionModel> result,
                                               List<SubscriptionEntity> entities) {
        assertEquals(entities.size(), result.size());

        for (int i = 0; i < entities.size(); i++) {
            thenModelShouldMatchEntity(result.get(i), entities.get(i));
        }
    }

    private void thenModelShouldMatchEntity(SubscriptionModel model, SubscriptionEntity entity) {
        assertEquals(entity.getPlanCode(), model.getPlanCode());
        assertEquals(entity.getPlanType(), model.getPlanType());
        assertEquals(entity.getPrice(), model.getPrice());
    }
}