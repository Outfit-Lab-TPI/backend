package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.model.*;
import com.outfitlab.project.infrastructure.model.*;
import com.outfitlab.project.infrastructure.repositories.interfaces.CombinationAttemptJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.CombinationJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ActiveProfiles("test")
class CombinationAttemptRepositoryImplTest {
    @Mock
    private CombinationAttemptJpaRepository combinationAttemptJpaRepository;
    @Mock
    private UserJpaRepository userJpaRepository;
    @Mock
    private CombinationJpaRepository combinationJpaRepository;
    @InjectMocks
    private CombinationAttemptRepositoryImpl repository;


    @Test
    void shouldSaveWhenUserExistsAndCombinationExistsById() {
        CombinationAttemptModel model = givenModelWithUserAndCombinationId(1L, 10L);

        givenExistingUser(1L);
        givenExistingCombination(10L);
        givenSavedAttempt(99L);

        Long result = whenSave(model);

        thenAttemptIdShouldBe(result, 99L);
    }

    @Test
    void shouldSaveWhenCombinationFoundByPrendas() {
        CombinationAttemptModel model = givenModelWithPrendas(1L, 2L);

        givenExistingUser(1L);
        givenCombinationFoundByPrendas(1L, 2L, 50L);
        givenSavedAttempt(77L);

        Long result = whenSave(model);

        thenAttemptIdShouldBe(result, 77L);
    }

    @Test
    void shouldSaveWhenCombinationNotFoundAndCreateNewOne() {
        CombinationAttemptModel model = givenModelWithPrendas(1L, 2L);

        givenExistingUser(1L);
        givenCombinationNotFoundByPrendas();
        givenCreatedCombination(200L);
        givenSavedAttempt(88L);

        Long result = whenSave(model);

        thenAttemptIdShouldBe(result, 88L);
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        CombinationAttemptModel model = givenModelWithUserAndCombinationId(999L, 10L);

        givenUserNotFound(999L);

        assertThatThrownBy(() -> whenSave(model))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void shouldThrowWhenCombinationIdDoesNotExist() {
        CombinationAttemptModel model = givenModelWithUserAndCombinationId(1L, 500L);

        givenExistingUser(1L);
        givenCombinationIdNotFound(500L);

        assertThatThrownBy(() -> whenSave(model))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Combination not found");
    }

    @Test
    void shouldReturnAttemptsWhenFindAllByPrenda() {
        givenAttemptsByPrenda(1L, 2L, 10L);

        var result = whenFindAllByPrenda(10L);

        thenResultShouldNotBeEmpty(result);
    }

    @Test
    void shouldReturnAttemptsFromLastNDays() {
        givenAttemptsFromLastDays();

        var result = whenFindLastNDays(5);

        thenResultShouldNotBeEmpty(result);
    }

    @Test
    void shouldReturnAllAttempts() {
        givenAllAttempts();

        var result = whenFindAll();

        thenResultShouldNotBeEmpty(result);
    }

    @Test
    void shouldCallDeleteAllAttemptsByGarmentCode() {
        whenDeleteAllByGarment("PANT001");

        verify(combinationAttemptJpaRepository).deleteAllAttemptsByGarmentCode("PANT001");
    }

    private CombinationAttemptModel givenModelWithUserAndCombinationId(Long userId, Long combId) {
        UserModel user = new UserModel(userId, "test@mail.com");

        CombinationModel combination = new CombinationModel(null, null);
        combination.setId(combId);

        return new CombinationAttemptModel(user, combination, LocalDateTime.now(), "url");
    }

    private BrandModel givenMarca(String codigo) {
        BrandModel marca = new BrandModel();
        marca.setCodigoMarca(codigo);
        marca.setNombre("MarcaTest");
        return marca;
    }

    private CombinationAttemptModel givenModelWithPrendas(Long supId, Long infId) {
        UserModel user = new UserModel(1L, "user@mail.com");
        BrandModel marcaSup = givenMarca("MARCA-SUP");
        BrandModel marcaInf = givenMarca("MARCA-INF");

        PrendaModel sup = new PrendaModel(supId, "Sup", "img", marcaSup);
        PrendaModel inf = new PrendaModel(infId, "Inf", "img", marcaInf);

        CombinationModel comb = new CombinationModel(sup, inf);

        return new CombinationAttemptModel(user, comb, LocalDateTime.now(), "url");
    }
    private void givenExistingUser(Long id) {
        UserEntity user = new UserEntity();
        user.setId(id);
        when(userJpaRepository.findById(id)).thenReturn(Optional.of(user));
    }

    private void givenUserNotFound(Long id) {
        when(userJpaRepository.findById(id)).thenReturn(Optional.empty());
    }

    private void givenExistingCombination(Long id) {
        CombinationEntity comb = new CombinationEntity();
        comb.setId(id);
        when(combinationJpaRepository.findById(id)).thenReturn(Optional.of(comb));
    }

    private void givenCombinationIdNotFound(Long id) {
        when(combinationJpaRepository.findById(id)).thenReturn(Optional.empty());
    }

    private void givenCombinationFoundByPrendas(Long supId, Long infId, Long combId) {
        CombinationEntity comb = new CombinationEntity();
        comb.setId(combId);

        when(combinationJpaRepository.findByPrendas(supId, infId))
                .thenReturn(Optional.of(comb));
    }

    private void givenCombinationNotFoundByPrendas() {
        when(combinationJpaRepository.findByPrendas(any(), any())).thenReturn(Optional.empty());
    }

    private void givenCreatedCombination(Long id) {
        CombinationEntity comb = new CombinationEntity();
        comb.setId(id);

        when(combinationJpaRepository.save(any())).thenReturn(comb);
    }

    private void givenSavedAttempt(Long id) {
        CombinationAttemptEntity attempt = new CombinationAttemptEntity();
        attempt.setId(id);
        when(combinationAttemptJpaRepository.save(any())).thenReturn(attempt);
    }

    private void givenAttemptsByPrenda(Long supId, Long infId, Long attemptId) {
        CombinationAttemptEntity attempt = minimalAttempt(attemptId);
        when(combinationAttemptJpaRepository
                .findByCombination_PrendaSuperior_IdOrCombination_PrendaInferior_Id(any(), any()))
                .thenReturn(List.of(attempt));
    }

    private void givenAttemptsFromLastDays() {
        when(combinationAttemptJpaRepository.findByCreatedAtAfter(any()))
                .thenReturn(List.of(minimalAttempt(1L)));
    }

    private void givenAllAttempts() {
        when(combinationAttemptJpaRepository.findAll())
                .thenReturn(List.of(minimalAttempt(1L)));
    }

    private Long whenSave(CombinationAttemptModel model) {
        return repository.save(model);
    }

    private List<CombinationAttemptModel> whenFindAllByPrenda(Long id) {
        return repository.findAllByPrenda(id);
    }

    private List<CombinationAttemptModel> whenFindLastNDays(int days) {
        return repository.findLastNDays(days);
    }

    private List<CombinationAttemptModel> whenFindAll() {
        return repository.findAll();
    }

    private void whenDeleteAllByGarment(String garmentCode) {
        repository.deleteAllByAttempsReltedToCombinationRelatedToGarments(garmentCode);
    }

    private void thenAttemptIdShouldBe(Long actual, Long expected) {
        assertThat(actual).isEqualTo(expected);
    }

    private void thenResultShouldNotBeEmpty(List<?> list) {
        assertThat(list).isNotEmpty();
    }

    private CombinationAttemptEntity minimalAttempt(Long id) {
        CombinationEntity combination = new CombinationEntity();
        combination.setId(10L);

        PrendaEntity sup = new PrendaEntity();
        sup.setId(1L);
        sup.setNombre("Sup");
        sup.setColor(new ColorEntity("Rojo", 1));
        sup.setMarca(new MarcaEntity("ADIDAS", "Adidas", "img", "site"));

        PrendaEntity inf = new PrendaEntity();
        inf.setId(2L);
        inf.setNombre("Inf");
        inf.setColor(new ColorEntity("Azul", 2));
        inf.setMarca(new MarcaEntity("NIKE", "Nike", "img", "site"));

        combination.setPrendaSuperior(sup);
        combination.setPrendaInferior(inf);

        CombinationAttemptEntity attempt = new CombinationAttemptEntity();
        attempt.setId(id);
        attempt.setCombination(combination);
        attempt.setCreatedAt(LocalDateTime.now());

        return attempt;
    }

}