package com.outfitlab.project.domain.useCases.recomendations;

import com.outfitlab.project.domain.interfaces.repositories.GarmentRecomendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CreateSugerenciasByGarmentsCodeTest {

    private GarmentRecomendationRepository garmentRecomendationRepository = mock(GarmentRecomendationRepository.class);
    private CreateSugerenciasByGarmentsCode createSugerenciasByGarmentsCode;

    private final String VALID_GARMENT_CODE = "JEAN_AZUL";
    private final String VALID_TYPE = "top";
    private final List<String> VALID_SUGERENCIAS = List.of("G002", "G003");
    private final String EMPTY_CODE = "";
    private final List<String> EMPTY_SUGERENCIAS = Collections.emptyList();

    @BeforeEach
    void setUp() {
        createSugerenciasByGarmentsCode = new CreateSugerenciasByGarmentsCode(garmentRecomendationRepository);
    }


    @Test
    public void shouldCallRepositoryWithAllValidParameters() {
        whenExecuteCreateSugerencias(VALID_GARMENT_CODE, VALID_TYPE, VALID_SUGERENCIAS);

        thenRepositoryWasCalled(VALID_GARMENT_CODE, VALID_TYPE, VALID_SUGERENCIAS, 1);
    }

    @Test
    public void shouldCallRepositoryWhenSugerenciasListIsEmpty() {
        whenExecuteCreateSugerencias(VALID_GARMENT_CODE, VALID_TYPE, EMPTY_SUGERENCIAS);

        thenRepositoryWasCalled(VALID_GARMENT_CODE, VALID_TYPE, EMPTY_SUGERENCIAS, 1);
    }

    @Test
    public void shouldCallRepositoryWhenGarmentCodeIsEmpty() {
        whenExecuteCreateSugerencias(EMPTY_CODE, VALID_TYPE, VALID_SUGERENCIAS);

        thenRepositoryWasCalled(EMPTY_CODE, VALID_TYPE, VALID_SUGERENCIAS, 1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException();

        assertThrows(RuntimeException.class,
                () -> whenExecuteCreateSugerencias(VALID_GARMENT_CODE, VALID_TYPE, VALID_SUGERENCIAS),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryWasCalled(VALID_GARMENT_CODE, VALID_TYPE, VALID_SUGERENCIAS, 1);
    }


    //privadoss
    private void givenRepositoryThrowsRuntimeException() {
        doThrow(new RuntimeException("DB Connection error"))
                .when(garmentRecomendationRepository).createSugerenciasByGarmentCode(anyString(), anyString(), anyList());
    }

    private void whenExecuteCreateSugerencias(String code, String type, List<String> sugerencias) {
        createSugerenciasByGarmentsCode.execute(code, type, sugerencias);
    }

    private void thenRepositoryWasCalled(String expectedCode, String expectedType, List<String> expectedSugerencias, int times) {
        verify(garmentRecomendationRepository, times(times)).createSugerenciasByGarmentCode(expectedCode, expectedType, expectedSugerencias);
    }
}