package com.outfitlab.project.domain.useCases.dashboard;

import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import com.outfitlab.project.domain.model.CombinationAttemptModel;
import com.outfitlab.project.domain.model.dto.PrendaDashboardDTO.DiaPrueba;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetActividadPorDiasTest {

    private CombinationAttemptRepository combinationAttemptRepository = mock(CombinationAttemptRepository.class);
    private GetActividadPorDias getActividadPorDias;

    private final int DAYS_TO_CHECK = 30;

    @BeforeEach
    void setUp() {
        getActividadPorDias = new GetActividadPorDias(combinationAttemptRepository);
    }


    @Test
    public void shouldReturnCorrectCountsWhenAttemptsAreSpreadAcrossDays() {
        List<CombinationAttemptModel> mockAttempts = givenAttemptsExistOnSpecificDays(1, 1, 3, 15, 2, 30);
        givenRepositoryReturnsAttempts(mockAttempts);

        List<DiaPrueba> result = whenExecuteGetActividad();

        thenRepositoryFindLastNDaysWasCalled(DAYS_TO_CHECK);

        List<DiaPrueba> expected = List.of(
                createDiaPrueba(1, 1),
                createDiaPrueba(15, 3),
                createDiaPrueba(30, 2)
        );
        thenDailyCountsAreCorrect(result, expected);
    }

    @Test
    public void shouldReturn30DaysWithZeroCountsWhenNoAttemptsAreFound() {
        givenRepositoryReturnsEmptyAttempts();

        List<DiaPrueba> result = whenExecuteGetActividad();

        thenRepositoryFindLastNDaysWasCalled(DAYS_TO_CHECK);
        thenAllDaysHaveZeroCount(result);
    }

    @Test
    public void shouldCountAllAttemptsCorrectlyInSingleDay() {
        List<CombinationAttemptModel> mockAttempts = givenAttemptsExistOnSpecificDays(5, 1);
        givenRepositoryReturnsAttempts(mockAttempts);

        List<DiaPrueba> result = whenExecuteGetActividad();

        thenDailyCountsAreCorrect(result, List.of(createDiaPrueba(1, 5)));
    }

    @Test
    public void shouldCountAttemptsOnBoundaryDaysCorrectly() {
        List<CombinationAttemptModel> mockAttempts = givenAttemptsExistOnSpecificDays(2, 1, 4, 30);
        givenRepositoryReturnsAttempts(mockAttempts);

        List<DiaPrueba> result = whenExecuteGetActividad();

        List<DiaPrueba> expected = List.of(
                createDiaPrueba(1, 2),
                createDiaPrueba(30, 4)
        );
        thenDailyCountsAreCorrect(result, expected);
    }

    @Test
    public void shouldIgnoreAttemptsWithInvalidDayOfMonth() {
        List<CombinationAttemptModel> mockAttempts = new ArrayList<>();
        mockAttempts.add(createAttempt(10));
        mockAttempts.add(createAttempt(31));

        givenRepositoryReturnsAttempts(mockAttempts);

        List<DiaPrueba> result = whenExecuteGetActividad();

        thenDailyCountsAreCorrect(result, List.of(createDiaPrueba(10, 1)));
    }


    //privadoss
    private void givenRepositoryReturnsAttempts(List<CombinationAttemptModel> attempts) {
        when(combinationAttemptRepository.findLastNDays(DAYS_TO_CHECK)).thenReturn(attempts);
    }

    private void givenRepositoryReturnsEmptyAttempts() {
        when(combinationAttemptRepository.findLastNDays(DAYS_TO_CHECK)).thenReturn(Collections.emptyList());
    }

    private CombinationAttemptModel createAttempt(int dayOfMonth) {
        CombinationAttemptModel attempt = mock(CombinationAttemptModel.class);

        LocalDateTime date = LocalDateTime.of(2025, 1, dayOfMonth, 10, 0);
        when(attempt.getCreatedAt()).thenReturn(date);
        return attempt;
    }

    private List<CombinationAttemptModel> givenAttemptsExistOnSpecificDays(int... countDayPairs) {
        List<CombinationAttemptModel> attempts = new ArrayList<>();
        IntStream.range(0, countDayPairs.length / 2)
                .forEach(i -> {
                    int count = countDayPairs[i * 2];
                    int day = countDayPairs[i * 2 + 1];

                    for (int j = 0; j < count; j++) {
                        attempts.add(createAttempt(day));
                    }
                });
        return attempts;
    }

    private List<DiaPrueba> whenExecuteGetActividad() {
        return getActividadPorDias.execute();
    }

    private DiaPrueba createDiaPrueba(int dia, int cantidad) {
        return new DiaPrueba(dia, cantidad);
    }

    private void thenRepositoryFindLastNDaysWasCalled(int days) {
        verify(combinationAttemptRepository, times(1)).findLastNDays(days);
    }

    private void thenDailyCountsAreCorrect(List<DiaPrueba> actualResult, List<DiaPrueba> expectedNonZeroDays) {
        assertEquals(DAYS_TO_CHECK, actualResult.size(), "La lista debe tener exactamente 30 días.");

        java.util.Map<Integer, Integer> actualCounts = new java.util.HashMap<>();
        actualResult.forEach(dp -> actualCounts.put(dp.dia(), dp.pruebas()));

        expectedNonZeroDays.forEach(expectedDP -> {
            assertTrue(actualCounts.containsKey(expectedDP.dia()), "El día " + expectedDP.dia() + " debe existir en el resultado.");

            assertEquals(expectedDP.pruebas(), actualCounts.get(expectedDP.dia()), "El día " + expectedDP.dia() + " debe tener el conteo correcto.");
        });
    }

    private void thenAllDaysHaveZeroCount(List<DiaPrueba> actualResult) {
        assertEquals(DAYS_TO_CHECK, actualResult.size(), "La lista debe tener 30 días.");
        actualResult.forEach(dp -> assertEquals(0, dp.pruebas(), "Todos los días deben tener conteo cero."));
    }
}