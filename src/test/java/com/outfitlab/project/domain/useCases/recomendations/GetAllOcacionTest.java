package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.OcacionRepository;
import com.outfitlab.project.domain.model.OcasionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetAllOcacionTest {

    private OcacionRepository ocacionRepository = mock(OcacionRepository.class);
    private GetAllOcacion getAllOcacion;

    private final int OCASION_COUNT = 4;

    @BeforeEach
    void setUp() {
        getAllOcacion = new GetAllOcacion(ocacionRepository);
    }


    @Test
    public void shouldReturnListOfOcasionesWhenOcasionesAreFound() {
        List<OcasionModel> expectedOcasiones = givenRepositoryReturnsOcasiones(OCASION_COUNT);

        List<OcasionModel> result = whenExecuteGetAllOcasion();

        thenResultMatchesExpectedList(result, expectedOcasiones, OCASION_COUNT);
        thenRepositoryFindAllOcasionesWasCalled(1);
    }

    @Test
    public void shouldReturnEmptyListWhenNoOcasionesAreFound() {
        List<OcasionModel> expectedEmptyList = givenRepositoryReturnsEmptyList();

        List<OcasionModel> result = whenExecuteGetAllOcasion();

        thenResultMatchesExpectedList(result, expectedEmptyList, 0);
        thenRepositoryFindAllOcasionesWasCalled(1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException();

        assertThrows(RuntimeException.class,
                () -> whenExecuteGetAllOcasion(),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryFindAllOcasionesWasCalled(1);
    }


    //privadoss
    private List<OcasionModel> givenRepositoryReturnsOcasiones(int count) {
        List<OcasionModel> mockList = Collections.nCopies(count, mock(OcasionModel.class));
        when(ocacionRepository.findAllOcasiones()).thenReturn(mockList);
        return mockList;
    }

    private List<OcasionModel> givenRepositoryReturnsEmptyList() {
        List<OcasionModel> emptyList = Collections.emptyList();
        when(ocacionRepository.findAllOcasiones()).thenReturn(emptyList);
        return emptyList;
    }

    private void givenRepositoryThrowsRuntimeException() {
        doThrow(new RuntimeException("Simulated DB error"))
                .when(ocacionRepository).findAllOcasiones();
    }

    private List<OcasionModel> whenExecuteGetAllOcasion() {
        return this.getAllOcacion.execute();
    }

    private void thenResultMatchesExpectedList(List<OcasionModel> actual, List<OcasionModel> expected, int expectedCount) {
        assertNotNull(actual, "La lista resultante no debe ser nula.");
        assertEquals(expectedCount, actual.size(), "El tamaño de la lista debe coincidir.");
        assertEquals(expected, actual, "El contenido de la lista debe coincidir con la lista simulada.");
    }

    private void thenRepositoryFindAllOcasionesWasCalled(int times) {
        verify(ocacionRepository, times(times)).findAllOcasiones();
    }
}