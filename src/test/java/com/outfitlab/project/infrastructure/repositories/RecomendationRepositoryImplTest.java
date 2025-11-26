package com.outfitlab.project.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.*;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;
import com.outfitlab.project.infrastructure.model.GarmentRecomendationEntity;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.GarmentJpaRepository;
import com.outfitlab.project.infrastructure.repositories.interfaces.RecomendationJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ActiveProfiles("test")
class RecomendationRepositoryImplTest {
    @Mock
    private RecomendationJpaRepository recomendationJpaRepository;

    @Mock
    private GarmentJpaRepository garmentJpaRepository;

    @InjectMocks
    private RecomendationRepositoryImpl repository;

    @Test
    void shouldFindRecomendationsByGarmentCode() throws GarmentNotFoundException {
        PrendaEntity garment = givenPrendaEntity("G1", "SUPERIOR");
        List<GarmentRecomendationEntity> entities = givenRecomendationEntities(garment, 2);
        givenFindByTopGarment(garment, entities);

        List<GarmentRecomendationModel> result = whenFindRecomendations("G1");

        thenResultShouldHaveSize(result, 2);
    }

    @Test
    void shouldThrowWhenGarmentNotFound() {
        givenGarmentNotFound("INVALID");

        assertThatThrownBy(() -> whenFindRecomendations("INVALID"))
                .isInstanceOf(GarmentNotFoundException.class)
                .hasMessageContaining("No encontramos la prenda con el código");
    }

    @Test
    void shouldDeleteRecomendationsByGarmentCode() {
        whenDeleteRecomendations("G001");

        thenDeleteShouldBeCalled("G001");
    }

    @Test
    void shouldCreateSugerenciasByGarmentCode() throws GarmentNotFoundException {
        PrendaEntity principal = givenPrendaEntity("G001", "SUPERIOR");
        List<String> sugerencias = List.of("G002", "G003");
        givenGarmentsExist(sugerencias);
        givenSavedRecomendations();

        whenCreateSugerencias("G001", "inferior", sugerencias);

        thenSaveAllShouldBeCalled();
    }

    @Test
    void shouldThrowWhenInvalidTypeInCreateSugerencias() {
        PrendaEntity principal = givenPrendaEntity("G001", "SUPERIOR");
        List<String> sugerencias = List.of("G002");

        givenGarmentsExist(sugerencias);

        assertThatThrownBy(() -> whenCreateSugerencias("G001", "INVALID", sugerencias))
                .isInstanceOf(GarmentNotFoundException.class)
                .hasMessageContaining("Tipo de prenda inválido");
    }

    @Test
    void shouldDeleteRecomendationByGarmentsCodeWhenTypeIsInferior() {
        whenDeleteRecomendation("G001", "G002", "inferior");

        thenDeleteWhenPrimaryIsBottomCalled("G001", "G002");
    }

    @Test
    void shouldDeleteRecomendationByGarmentsCodeWhenTypeIsSuperior() {
        whenDeleteRecomendation("G001", "G002", "superior");

        thenDeleteWhenPrimaryIsTopCalled("G001", "G002");
    }

    private PrendaEntity givenPrendaEntity(String code, String tipo) {
        PrendaEntity p = new PrendaEntity();
        p.setGarmentCode(code);
        p.setTipo(tipo);
        when(garmentJpaRepository.findByGarmentCode(code)).thenReturn(p);
        return p;
    }

    private void givenGarmentNotFound(String code) {
        when(garmentJpaRepository.findByGarmentCode(code)).thenReturn(null);
    }

    private List<GarmentRecomendationEntity> givenRecomendationEntities(PrendaEntity garment, int count) {
        List<GarmentRecomendationEntity> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            GarmentRecomendationEntity entity = new GarmentRecomendationEntity();

            // Asumimos que si la prenda principal es SUPERIOR, ponemos la inferior ficticia
            PrendaEntity other = new PrendaEntity();
            other.setGarmentCode("DUMMY" + i);
            other.setTipo(garment.getTipo().equalsIgnoreCase("SUPERIOR") ? "INFERIOR" : "SUPERIOR");
            other.setMarca(new com.outfitlab.project.infrastructure.model.MarcaEntity("X"+i, "MarcaDummy"+i));

            if (garment.getTipo().equalsIgnoreCase("SUPERIOR")) {
                entity.setTopGarment(garment);
                entity.setBottomGarment(other);
            } else {
                entity.setBottomGarment(garment);
                entity.setTopGarment(other);
            }

            list.add(entity);
        }
        return list;
    }

    private void givenFindByTopGarment(PrendaEntity garment, List<GarmentRecomendationEntity> result) {
        when(recomendationJpaRepository.findByTopGarment(garment)).thenReturn(result);
    }

    private void givenGarmentsExist(List<String> codes) {
        for (String code : codes) {
            givenPrendaEntity(code, "INFERIOR");
        }
    }

    private void givenSavedRecomendations() {
        when(recomendationJpaRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
    }

    private List<GarmentRecomendationModel> whenFindRecomendations(String code) throws GarmentNotFoundException {
        return repository.findRecomendationsByGarmentCode(code);
    }

    private void whenDeleteRecomendations(String code) {
        repository.deleteRecomendationsByGarmentCode(code);
    }

    private void whenCreateSugerencias(String code, String type, List<String> sugerencias) throws GarmentNotFoundException {
        repository.createSugerenciasByGarmentCode(code, type, sugerencias);
    }

    private void whenDeleteRecomendation(String primary, String secondary, String type) {
        repository.deleteRecomendationByGarmentsCode(primary, secondary, type);
    }

    private void thenResultShouldHaveSize(List<?> list, int size) {
        assertThat(list).hasSize(size);
    }

    private void thenDeleteShouldBeCalled(String code) {
        verify(recomendationJpaRepository).deleteAllByGarmentCode(code);
    }

    private void thenSaveAllShouldBeCalled() {
        verify(recomendationJpaRepository).saveAll(anyList());
    }

    private void thenDeleteWhenPrimaryIsBottomCalled(String primary, String secondary) {
        verify(recomendationJpaRepository).deleteWhenPrimaryIsBottom(primary, secondary);
    }

    private void thenDeleteWhenPrimaryIsTopCalled(String primary, String secondary) {
        verify(recomendationJpaRepository).deleteWhenPrimaryIsTop(primary, secondary);
    }
}