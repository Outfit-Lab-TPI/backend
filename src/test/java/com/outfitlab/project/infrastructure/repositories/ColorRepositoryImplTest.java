package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.ColorNotFoundException;
import com.outfitlab.project.domain.model.ColorModel;
import com.outfitlab.project.infrastructure.model.ColorEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.ColorJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ActiveProfiles("test")
class ColorRepositoryImplTest {

    @Mock
    private ColorJpaRepository colorJpaRepository;

    @InjectMocks
    private ColorRepositoryImpl repository;

    @Test
    void shouldReturnColorsWhenTheyExist() {
        givenExistingColors("ROJO");

        var result = whenFindAllColores();

        thenFirstColorShouldBe(result, "ROJO");
        verify(colorJpaRepository).findAll();
    }

    @Test
    void shouldThrowExceptionWhenColorsListIsEmpty() {
        givenEmptyColors();

        assertThatThrownBy(this::whenFindAllColores)
                .isInstanceOf(ColorNotFoundException.class)
                .hasMessageContaining("No encontramos ocasiones.");

        verify(colorJpaRepository).findAll();
    }
    private void givenExistingColors(String nombreColor) {
        ColorEntity entity = new ColorEntity();
        entity.setId(1L);
        entity.setNombre(nombreColor);

        when(colorJpaRepository.findAll()).thenReturn(List.of(entity));
    }

    private void givenEmptyColors() {
        when(colorJpaRepository.findAll()).thenReturn(List.of());
    }

    private List<ColorModel> whenFindAllColores() {
        return repository.findAllColores();
    }

    private void thenFirstColorShouldBe(List<ColorModel> result, String nombreEsperado) {
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getNombre()).isEqualTo(nombreEsperado);
    }
}
