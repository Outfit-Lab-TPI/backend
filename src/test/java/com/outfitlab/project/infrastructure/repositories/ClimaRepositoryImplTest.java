package com.outfitlab.project.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.*;

import com.outfitlab.project.domain.exceptions.ClimaNotFoundException;
import com.outfitlab.project.domain.model.ClimaModel;
import com.outfitlab.project.infrastructure.model.ClimaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.ClimaJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
@SpringBootTest
@ActiveProfiles("test")
class ClimaRepositoryImplTest {
    @Mock
    private ClimaJpaRepository climaJpaRepository;

    @InjectMocks
    private ClimaRepositoryImpl repository;

    @Test
    void shouldReturnClimasWhenTheyExist() {
        givenExistingClimas("FRIO");

        var result = whenFindAllClimas();

        thenFirstClimaShouldBe(result, "FRIO");
        verify(climaJpaRepository).findAll();
    }

    @Test
    void shouldThrowExceptionWhenClimasListIsEmpty() {
        givenEmptyClimas();

        assertThatThrownBy(this::whenFindAllClimas)
                .isInstanceOf(ClimaNotFoundException.class)
                .hasMessageContaining("No encontramos climas.");

        verify(climaJpaRepository).findAll();
    }

    private void givenExistingClimas(String nombre) {
        ClimaEntity clima = new ClimaEntity();
        clima.setId(1L);
        clima.setNombre(nombre);

        when(climaJpaRepository.findAll()).thenReturn(List.of(clima));
    }

    private void givenEmptyClimas() {
        when(climaJpaRepository.findAll()).thenReturn(List.of());
    }

    private List<ClimaModel> whenFindAllClimas() {
        return repository.findAllClimas();
    }

    private void thenFirstClimaShouldBe(List<ClimaModel> result, String nombreEsperado) {
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getNombre()).isEqualTo(nombreEsperado);
    }
}