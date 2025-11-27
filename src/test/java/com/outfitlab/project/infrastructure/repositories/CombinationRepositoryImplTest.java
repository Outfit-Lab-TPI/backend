package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.CombinationModel;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.infrastructure.model.CombinationEntity;
import com.outfitlab.project.infrastructure.model.MarcaEntity;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.CombinationJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ActiveProfiles("test")
class CombinationRepositoryImplTest {
    @Mock
    private CombinationJpaRepository jpaRepository;

    @InjectMocks
    private CombinationRepositoryImpl repository;

    @Test
    void shouldFindById() {
        givenExistingCombination(10L);

        Optional<CombinationModel> result = whenFindById(10L);

        thenResultShouldBePresent(result);
    }

    @Test
    void shouldReturnEmptyWhenIdNotFound() {
        givenCombinationNotFoundById(50L);

        Optional<CombinationModel> result = whenFindById(50L);

        thenResultShouldBeEmpty(result);
    }

    @Test
    void shouldFindByPrendas() {
        givenExistingCombinationByPrendas(1L, 2L, 100L);

        Optional<CombinationModel> result = whenFindByPrendas(1L, 2L);

        thenResultShouldBePresent(result);
    }

    @Test
    void shouldReturnEmptyWhenPrendasNotFound() {
        givenCombinationNotFoundByPrendas();

        Optional<CombinationModel> result = whenFindByPrendas(1L, 2L);

        thenResultShouldBeEmpty(result);
    }

    @Test
    void shouldSave() {
        CombinationModel model = givenCombinationModel(5L, 6L);
        givenSavedCombination(200L);

        CombinationModel saved = whenSave(model);

        thenCombinationIdShouldBe(saved, 200L);
    }

    @Test
    void shouldDeleteAllByGarmentCode() {
        whenDeleteAllByGarment("X123");

        thenDeleteShouldBeCalled("X123");
    }

    private void givenExistingCombination(Long id) {
        CombinationEntity entity = minimalCombinationEntity(id);
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
    }

    private void givenCombinationNotFoundById(Long id) {
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());
    }

    private void givenExistingCombinationByPrendas(Long supId, Long infId, Long combId) {
        CombinationEntity entity = minimalCombinationEntity(combId);
        when(jpaRepository.findByPrendas(supId, infId)).thenReturn(Optional.of(entity));
    }

    private void givenCombinationNotFoundByPrendas() {
        when(jpaRepository.findByPrendas(any(), any())).thenReturn(Optional.empty());
    }

    private void givenSavedCombination(Long id) {
        CombinationEntity saved = minimalCombinationEntity(id);
        when(jpaRepository.save(any())).thenReturn(saved);
    }

    private Optional<CombinationModel> whenFindById(Long id) {
        return repository.findById(id);
    }

    private Optional<CombinationModel> whenFindByPrendas(Long supId, Long infId) {
        return repository.findByPrendas(supId, infId);
    }

    private CombinationModel whenSave(CombinationModel model) {
        return repository.save(model);
    }

    private void whenDeleteAllByGarment(String code) {
        repository.deleteAllByGarmentcode(code);
    }

    private void thenResultShouldBePresent(Optional<?> result) {
        assertThat(result).isPresent();
    }

    private void thenResultShouldBeEmpty(Optional<?> result) {
        assertThat(result).isEmpty();
    }

    private void thenCombinationIdShouldBe(CombinationModel model, Long expected) {
        assertThat(model.getId()).isEqualTo(expected);
    }

    private void thenDeleteShouldBeCalled(String code) {
        verify(jpaRepository).deleteAllByGarmentCode(code);
    }

    private CombinationEntity minimalCombinationEntity(Long id) {
        PrendaEntity sup = new PrendaEntity();
        sup.setId(1L);
        sup.setMarca(new MarcaEntity("A", "MarcaA"));

        PrendaEntity inf = new PrendaEntity();
        inf.setId(2L);
        inf.setMarca(new MarcaEntity("B", "MarcaB"));

        CombinationEntity entity = new CombinationEntity(sup, inf);
        entity.setId(id);
        return entity;
    }

    private BrandModel givenMarca(String code) {
        BrandModel m = new BrandModel();
        m.setCodigoMarca(code);
        m.setNombre("MarcaTest");
        return m;
    }

    private CombinationModel givenCombinationModel(Long supId, Long infId) {
        PrendaModel sup = new PrendaModel(supId, "Sup", "img", givenMarca("MS"));
        PrendaModel inf = new PrendaModel(infId, "Inf", "img", givenMarca("MI"));

        return new CombinationModel(sup, inf);
    }
}