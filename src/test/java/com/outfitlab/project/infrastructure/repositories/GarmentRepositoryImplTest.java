package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.infrastructure.model.*;
import com.outfitlab.project.infrastructure.repositories.interfaces.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class GarmentRepositoryImplTest {
    @Mock
    private GarmentJpaRepository garmentJpaRepository;
    @Mock
    private BrandJpaRepository brandJpaRepository;
    @Mock
    private ColorJpaRepository colorJpaRepository;
    @Mock
    private ClimaJpaRepository climaJpaRepository;
    @Mock
    private OcasionJpaRepository ocasionJpaRepository;

    @InjectMocks
    private GarmentRepositoryImpl repository;

    @Test
    void shouldFindByBrandCodeAndTipo() {
        givenGarmentsPage("brand1", "superior", 0);

        PageDTO result = whenFindByBrandCodeAndTipo("brand1", "superior", 0);

        thenPageDTOShouldBeCorrect(result, 1);
    }

    @Test
    void shouldThrowWhenGarmentCodeNotFound() {
        givenGarmentNotFound("G123");

        assertThatThrownBy(() -> whenFindByGarmentCode("G123"))
                .isInstanceOf(GarmentNotFoundException.class)
                .hasMessageContaining("No encontramos la prenda");
    }

    @Test
    void shouldFindGarmentByGarmentCode() {
        givenExistingGarment("G123");

        PrendaModel model = whenFindByGarmentCode("G123");

        thenGarmentCodeShouldBe(model, "G123");
    }

    @Test
    void shouldCreateGarmentSuccessfully() {
        givenBrandExists("B1");
        givenColorExists("Red");
        givenClimaExists("Frio");
        givenOcasionExists("Casual");

        whenCreateGarment("Prenda1", "G123", "superior", "Red", "B1", "img.png", "Frio",
                List.of("Casual"), "Hombre");

        thenGarmentShouldBeSaved();
    }

    @Test
    void shouldUpdateGarmentSuccessfully() {
        givenExistingGarmentEntity("G123");
        givenColorExists("Blue");
        givenClimaExists("Calor");
        givenOcasionExists("Formal");

        whenUpdateGarment("Prenda2", "inferior", "Blue", "Formal", "G123", "newImg.png", "G124", "Calor",
                List.of("Formal"), "Mujer");

        thenGarmentShouldBeSaved();
    }

    @Test
    void shouldDeleteGarmentSuccessfully() {
        String code = "G123";

        whenDeleteGarment(code);

        thenDeleteByGarmentCodeShouldBeCalled(code);
    }

    private void givenGarmentsPage(String brandCode, String tipo, int page) {
        // Marca
        MarcaEntity brand = new MarcaEntity();
        brand.setCodigoMarca(brandCode);
        brand.setNombre("Brand Name");

        // Color
        ColorEntity color = new ColorEntity();
        color.setNombre("Rojo");
        color.setValor(1);

        // Clima
        ClimaEntity clima = new ClimaEntity();
        clima.setNombre("Frio");

        // Prenda
        PrendaEntity entity = new PrendaEntity();
        entity.setGarmentCode("G1");
        entity.setNombre("Prenda Test");
        entity.setMarca(brand);
        entity.setColor(color);
        entity.setClimaAdecuado(clima);
        entity.setTipo(tipo);

        Page<PrendaEntity> pageResult = new PageImpl<>(List.of(entity), PageRequest.of(page, 10), 1);

        when(garmentJpaRepository.findByMarca_CodigoMarcaAndTipo(
                eq(brandCode),
                eq(tipo.toLowerCase()),
                any(PageRequest.class)
        )).thenReturn(pageResult);
    }

    private void givenGarmentNotFound(String garmentCode) {
        when(garmentJpaRepository.findByGarmentCode(garmentCode)).thenReturn(null);
    }

    private void givenExistingGarment(String garmentCode) {
        PrendaEntity entity = new PrendaEntity();
        entity.setGarmentCode(garmentCode);
        when(garmentJpaRepository.findByGarmentCode(garmentCode)).thenReturn(entity);
    }

    private void givenExistingGarmentEntity(String garmentCode) {
        PrendaEntity entity = new PrendaEntity();
        entity.setGarmentCode(garmentCode);
        when(garmentJpaRepository.findByGarmentCode(garmentCode)).thenReturn(entity);
    }

    private void givenBrandExists(String brandCode) {
        MarcaEntity brand = new MarcaEntity(brandCode, "BrandName");
        when(brandJpaRepository.findByCodigoMarca(brandCode)).thenReturn(brand);
    }

    private void givenColorExists(String colorNombre) {
        ColorEntity color = new ColorEntity();
        color.setNombre(colorNombre);
        when(colorJpaRepository.findColorEntityByNombre(colorNombre)).thenReturn(Optional.of(color));
    }

    private void givenClimaExists(String climaNombre) {
        ClimaEntity clima = new ClimaEntity();
        clima.setNombre(climaNombre);
        when(climaJpaRepository.findClimaEntityByNombre(climaNombre)).thenReturn(Optional.of(clima));
    }

    private void givenOcasionExists(String ocasionNombre) {
        OcasionEntity ocasion = new OcasionEntity();
        ocasion.setNombre(ocasionNombre);
        when(ocasionJpaRepository.findOcasionEntityByNombre(ocasionNombre)).thenReturn(Optional.of(ocasion));
    }

    private PageDTO whenFindByBrandCodeAndTipo(String brandCode, String tipo, int page) {
        return repository.findByBrandCodeAndTipo(brandCode, tipo, page);
    }

    private PrendaModel whenFindByGarmentCode(String code) {
        return repository.findByGarmentCode(code);
    }

    private void whenCreateGarment(String name, String garmentCode, String type, String colorNombre,
                                   String brandCode, String imageUrl, String climaNombre, List<String> ocasiones, String genero) {
        repository.createGarment(name, garmentCode, type, colorNombre, brandCode, imageUrl, climaNombre, ocasiones, genero);
    }

    private void whenUpdateGarment(String name, String type, String colorNombre, String event, String garmentCode,
                                   String imageUrl, String newGarmentCode, String climaNombre, List<String> ocasiones, String genero) {
        repository.updateGarment(name, type, colorNombre, event, garmentCode, imageUrl, newGarmentCode, climaNombre, ocasiones, genero);
    }

    private void whenDeleteGarment(String garmentCode) {
        repository.deleteGarment(garmentCode);
    }

    private void thenPageDTOShouldBeCorrect(PageDTO dto, int expectedSize) {
        assertThat(dto.getContent()).hasSize(expectedSize);
        assertThat(dto.getPage()).isZero();
        assertThat(dto.getSize()).isEqualTo(10);
        assertThat(dto.getTotalElements()).isEqualTo(1);
    }

    private void thenGarmentCodeShouldBe(PrendaModel model, String code) {
        assertThat(model.getGarmentCode()).isEqualTo(code);
    }

    private void thenGarmentShouldBeSaved() {
        verify(garmentJpaRepository).save(any(PrendaEntity.class));
    }

    private void thenDeleteByGarmentCodeShouldBeCalled(String code) {
        verify(garmentJpaRepository).deleteByGarmentCode(code);
    }


}