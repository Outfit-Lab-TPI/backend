package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.infrastructure.model.MarcaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.BrandJpaRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


import static org.mockito.Mockito.*;



@SpringBootTest
@ActiveProfiles("test") // IMPORTANTISIMO GENTE ESTE ACTIVATE PROFILE PARA NO AFECTAR LA BDD DE RENDER, PRESTAR ATENCIÓN
class BrandRepositoryImplTest {

    @Mock
    private BrandJpaRepository brandJpaRepository;

    @InjectMocks
    private BrandRepositoryImpl repository;

    @Test
    void shouldReturnBrandModelWhenBrandCodeExists() {
        givenExistingBrand("NIKE", "Nike");

        BrandModel result = whenFindByBrandCode("NIKE");

        thenBrandShouldBe(result, "NIKE", "Nike");
        verify(brandJpaRepository).findByCodigoMarca("NIKE");
    }

    @Test
    void shouldThrowExceptionWhenBrandCodeDoesNotExist() {
        givenNonExistingBrand("XYZ");

        assertThatThrownBy(() -> whenFindByBrandCode("XYZ"))
                .isInstanceOf(BrandsNotFoundException.class)
                .hasMessageContaining("No encontramos la marca.");

        verify(brandJpaRepository).findByCodigoMarca("XYZ");
    }

    @Test
    void shouldReturnBrandsPageWhenGettingAllBrands() {
        givenPageBrands("ADIDAS", "Adidas");

        Page<BrandModel> result = whenGetAllBrands(0);

        thenFirstBrandShouldBe(result, "ADIDAS");
        verify(brandJpaRepository).findAll(PageRequest.of(0, 10));
    }

    @Test
    void shouldCreateBrandAndReturnCode() {
        BrandModel model = givenBrandModel("PUMA", "Puma");

        givenSavedBrand("PUMA", "Puma");

        String result = whenCreateBrand(model);

        thenBrandCodeShouldBe(result, "PUMA");
        verify(brandJpaRepository).save(any(MarcaEntity.class));
    }

    private void givenExistingBrand(String code, String name) {
        MarcaEntity entity = new MarcaEntity(code, name, "logo.png", "site.com");
        when(brandJpaRepository.findByCodigoMarca(code)).thenReturn(entity);
    }

    private void givenNonExistingBrand(String code) {
        when(brandJpaRepository.findByCodigoMarca(code)).thenReturn(null);
    }

    private void givenPageBrands(String code, String name) {
        MarcaEntity entity = new MarcaEntity(code, name, "logo2.png", "site.com");
        Page<MarcaEntity> page = new PageImpl<>(List.of(entity));
        when(brandJpaRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
    }

    private BrandModel givenBrandModel(String code, String name) {
        return new BrandModel(code, name, "logo.png", "site.com");
    }

    private void givenSavedBrand(String code, String name) {
        MarcaEntity saved = new MarcaEntity(code, name, "logo.png", "site.com");
        when(brandJpaRepository.save(any(MarcaEntity.class))).thenReturn(saved);
    }

    private BrandModel whenFindByBrandCode(String code) {
        return repository.findByBrandCode(code);
    }

    private Page<BrandModel> whenGetAllBrands(int page) {
        return repository.getAllBrands(page);
    }

    private String whenCreateBrand(BrandModel model) {
        return repository.createBrand(model);
    }

    private void thenBrandShouldBe(BrandModel result, String code, String name) {
        assertThat(result).isNotNull();
        assertThat(result.getCodigoMarca()).isEqualTo(code);
        assertThat(result.getNombre()).isEqualTo(name);
    }

    private void thenFirstBrandShouldBe(Page<BrandModel> page, String expectedCode) {
        assertThat(page).isNotEmpty();
        assertThat(page.getContent().get(0).getCodigoMarca()).isEqualTo(expectedCode);
    }

    private void thenBrandCodeShouldBe(String result, String expectedCode) {
        assertThat(result).isEqualTo(expectedCode);
    }
}


