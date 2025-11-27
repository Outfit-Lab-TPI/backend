package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.ColorRepository;
import com.outfitlab.project.domain.model.ColorModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetAllColorsTest {

    private ColorRepository colorRepository = mock(ColorRepository.class);
    private GetAllColors getAllColors;

    private final int COLOR_COUNT = 5;

    @BeforeEach
    void setUp() {
        getAllColors = new GetAllColors(colorRepository);
    }


    @Test
    public void shouldReturnListOfColorsWhenColorsAreFound() {
        List<ColorModel> expectedColors = givenRepositoryReturnsColors(COLOR_COUNT);

        List<ColorModel> result = whenExecuteGetAllColors();

        thenResultMatchesExpectedList(result, expectedColors, COLOR_COUNT);
        thenRepositoryFindAllColoresWasCalled(1);
    }

    @Test
    public void shouldReturnEmptyListWhenNoColorsAreFound() {
        List<ColorModel> expectedEmptyList = givenRepositoryReturnsEmptyList();

        List<ColorModel> result = whenExecuteGetAllColors();

        thenResultMatchesExpectedList(result, expectedEmptyList, 0);
        thenRepositoryFindAllColoresWasCalled(1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException();

        assertThrows(RuntimeException.class,
                () -> whenExecuteGetAllColors(),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryFindAllColoresWasCalled(1);
    }


    //privadosss
    private List<ColorModel> givenRepositoryReturnsColors(int count) {
        List<ColorModel> mockList = Collections.nCopies(count, mock(ColorModel.class));
        when(colorRepository.findAllColores()).thenReturn(mockList);
        return mockList;
    }

    private List<ColorModel> givenRepositoryReturnsEmptyList() {
        List<ColorModel> emptyList = Collections.emptyList();
        when(colorRepository.findAllColores()).thenReturn(emptyList);
        return emptyList;
    }

    private void givenRepositoryThrowsRuntimeException() {
        doThrow(new RuntimeException("Simulated DB error"))
                .when(colorRepository).findAllColores();
    }

    private List<ColorModel> whenExecuteGetAllColors() {
        return getAllColors.execute();
    }

    private void thenResultMatchesExpectedList(List<ColorModel> actual, List<ColorModel> expected, int expectedCount) {
        assertNotNull(actual, "La lista resultante no debe ser nula.");
        assertEquals(expectedCount, actual.size(), "El tamaño de la lista debe coincidir.");
        assertEquals(expected, actual, "El contenido de la lista debe coincidir con la lista simulada.");
    }

    private void thenRepositoryFindAllColoresWasCalled(int times) {
        verify(colorRepository, times(times)).findAllColores();
    }
}