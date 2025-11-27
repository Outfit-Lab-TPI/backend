package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.ClimaRepository;
import com.outfitlab.project.domain.model.ClimaModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetAllClimaTest {

    private ClimaRepository climaRepository = mock(ClimaRepository.class);
    private GetAllClima getAllClima;

    private final int CLIMA_COUNT = 3;

    @BeforeEach
    void setUp() {
        getAllClima = new GetAllClima(climaRepository);
    }


    @Test
    public void shouldReturnListOfClimasWhenClimasAreFound() {
        List<ClimaModel> expectedClimas = givenRepositoryReturnsClimas(CLIMA_COUNT);

        List<ClimaModel> result = whenExecuteGetAllClima();

        thenResultMatchesExpectedList(result, expectedClimas, CLIMA_COUNT);
        thenRepositoryFindAllClimasWasCalled(1);
    }

    @Test
    public void shouldReturnEmptyListWhenNoClimasAreFound() {
        List<ClimaModel> expectedEmptyList = givenRepositoryReturnsEmptyList();

        List<ClimaModel> result = whenExecuteGetAllClima();

        thenResultMatchesExpectedList(result, expectedEmptyList, 0);
        thenRepositoryFindAllClimasWasCalled(1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException();

        assertThrows(RuntimeException.class,
                () -> whenExecuteGetAllClima(),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryFindAllClimasWasCalled(1);
    }


    //privadoss
    private List<ClimaModel> givenRepositoryReturnsClimas(int count) {
        List<ClimaModel> mockList = Collections.nCopies(count, mock(ClimaModel.class));
        when(climaRepository.findAllClimas()).thenReturn(mockList);
        return mockList;
    }

    private List<ClimaModel> givenRepositoryReturnsEmptyList() {
        List<ClimaModel> emptyList = Collections.emptyList();
        when(climaRepository.findAllClimas()).thenReturn(emptyList);
        return emptyList;
    }

    private void givenRepositoryThrowsRuntimeException() {
        doThrow(new RuntimeException("Simulated DB error"))
                .when(climaRepository).findAllClimas();
    }

    private List<ClimaModel> whenExecuteGetAllClima() {
        return getAllClima.execute();
    }

    private void thenResultMatchesExpectedList(List<ClimaModel> actual, List<ClimaModel> expected, int expectedCount) {
        assertNotNull(actual, "La lista resultante no debe ser nula.");
        assertEquals(expectedCount, actual.size(), "El tamaño de la lista debe coincidir.");
        assertEquals(expected, actual, "El contenido de la lista debe coincidir con la lista simulada.");
    }

    private void thenRepositoryFindAllClimasWasCalled(int times) {
        verify(climaRepository, times(times)).findAllClimas();
    }
}