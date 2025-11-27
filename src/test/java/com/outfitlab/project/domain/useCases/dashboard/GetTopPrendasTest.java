package com.outfitlab.project.domain.useCases.dashboard;

import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.CombinationAttemptModel;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.ColorModel;
import com.outfitlab.project.domain.model.dto.PrendaDashboardDTO.TopPrenda;
import com.outfitlab.project.domain.model.dto.PrendaDashboardDTO.DailyPrueba;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetTopPrendasTest {

    private GarmentRepository prendaRepository = mock(GarmentRepository.class);
    private CombinationAttemptRepository combinationAttemptRepository = mock(CombinationAttemptRepository.class);
    private GetTopPrendas getTopPrendas;

    private final LocalDate FIXED_TODAY = LocalDate.of(2025, 11, 27);
    private final LocalDateTime FIXED_NOW = FIXED_TODAY.atStartOfDay();

    private final String TARGET_BRAND = "ADIDAS";
    private final String OTHER_BRAND = "NIKE";
    private final int TOP_N_LIMIT = 2;

    @BeforeEach
    void setUp() {
        getTopPrendas = new GetTopPrendas(prendaRepository, combinationAttemptRepository);
    }


    @Test
    public void shouldReturnEmptyListWhenNoPrendasMatchBrandCode() {
        List<PrendaModel> allPrendas = List.of(createPrenda(1L, "T-Shirt", OTHER_BRAND));
        givenRepositoryReturnsAllPrendas(allPrendas);

        try (MockedStatic<LocalDate> mockedStatic = mockStatic(LocalDate.class)) {
            mockedStatic.when(LocalDate::now).thenReturn(FIXED_TODAY);

            List<TopPrenda> result = whenExecuteGetTopPrendas(TOP_N_LIMIT, TARGET_BRAND);

            thenResultSizeIsCorrect(result, 0);
        }
    }

    @Test
    public void shouldReturnPrendasWithZeroPruebasWhenNoAttemptsExist() {
        List<PrendaModel> allPrendas = List.of(createPrenda(1L, "T-Shirt", TARGET_BRAND));
        givenRepositoryReturnsAllPrendas(allPrendas);
        givenAttemptsExistForPrenda(1L, 0);

        try (MockedStatic<LocalDate> mockedStatic = mockStatic(LocalDate.class)) {
            mockedStatic.when(LocalDate::now).thenReturn(FIXED_TODAY);

            List<TopPrenda> result = whenExecuteGetTopPrendas(TOP_N_LIMIT, TARGET_BRAND);

            thenResultSizeIsCorrect(result, 1);
            assertEquals(0, result.get(0).pruebas(), "El conteo total debe ser cero.");
            thenDailyCountsAreCorrect(result.get(0).daily(), 0, 0);
        }
    }

    @Test
    public void shouldPropagateNPEWhenBrandModelIsNull() {
        List<PrendaModel> allPrendas = List.of(createPrenda(1L, "T-Shirt", null));
        givenRepositoryReturnsAllPrendas(allPrendas);

        try (MockedStatic<LocalDate> mockedStatic = mockStatic(LocalDate.class)) {
            mockedStatic.when(LocalDate::now).thenReturn(FIXED_TODAY);

            assertThrows(NullPointerException.class,
                    () -> whenExecuteGetTopPrendas(TOP_N_LIMIT, TARGET_BRAND),
                    "Debe fallar con NPE si la marca es null y se intenta acceder a su código.");
        }
    }


    //privadoss
    private void givenRepositoryReturnsAllPrendas(List<PrendaModel> prendas) {
        when(prendaRepository.findAll()).thenReturn(prendas);
    }

    private void givenAttemptsExistForPrenda(Long prendaId, int totalAttempts, int... countDayPairs) {
        List<CombinationAttemptModel> attempts = new ArrayList<>();

        for (int i = 0; i < countDayPairs.length; i += 2) {
            int count = countDayPairs[i];
            int daysAgo = countDayPairs[i + 1];

            LocalDateTime date = FIXED_NOW.minusDays(daysAgo);

            for (int j = 0; j < count; j++) {
                CombinationAttemptModel attempt = mock(CombinationAttemptModel.class);
                when(attempt.getCreatedAt()).thenReturn(date);
                attempts.add(attempt);
            }
        }

        if (attempts.size() != totalAttempts) {
            throw new IllegalArgumentException("El número de intentos simulados no coincide con el totalAttempts.");
        }

        when(combinationAttemptRepository.findAllByPrenda(prendaId)).thenReturn(attempts);
    }

    private PrendaModel createPrenda(Long id, String name, String brandCode) {
        PrendaModel prenda = mock(PrendaModel.class);

        BrandModel brand = null;
        if (brandCode != null) {
            brand = mock(BrandModel.class);
            when(brand.getCodigoMarca()).thenReturn(brandCode);
        }

        ColorModel color = mock(ColorModel.class);
        when(color.getNombre()).thenReturn("ColorName");

        when(prenda.getId()).thenReturn(id);
        when(prenda.getNombre()).thenReturn(name);
        when(prenda.getMarca()).thenReturn(brand);
        when(prenda.getColor()).thenReturn(color);
        when(prenda.getImagenUrl()).thenReturn("url/" + id);

        return prenda;
    }

    private List<TopPrenda> whenExecuteGetTopPrendas(int topN, String brandCode) {
        return getTopPrendas.execute(topN, brandCode);
    }

    private void thenResultSizeIsCorrect(List<TopPrenda> result, int size) {
        assertNotNull(result);
        assertEquals(size, result.size(), "El tamaño del resultado debe ser el esperado.");
    }

    private void thenResultIsSortedAndContentIsCorrect(List<TopPrenda> result, Long top1Id, int top1Pruebas, Long top2Id, int top2Pruebas) {
        assertTrue(result.get(0).pruebas() >= result.get(1).pruebas(), "El Top 1 debe tener más pruebas que el Top 2.");

        assertEquals(top1Id, result.get(0).id(), "El ID del Top 1 es incorrecto.");
        assertEquals(top1Pruebas, result.get(0).pruebas(), "El conteo de pruebas del Top 1 es incorrecto.");

        assertEquals(top2Id, result.get(1).id(), "El ID del Top 2 es incorrecto.");
        assertEquals(top2Pruebas, result.get(1).pruebas(), "El conteo de pruebas del Top 2 es incorrecto.");
    }

    private void thenDailyCountsAreCorrect(@NotNull List<DailyPrueba> daily, int todayCount, int yesterdayCount) {
        assertEquals(30, daily.size(), "La lista daily debe tener 30 días.");
        assertEquals(todayCount, daily.get(29).pruebas(), "El conteo de hoy debe ser el esperado.");
        assertEquals(yesterdayCount, daily.get(28).pruebas(), "El conteo de ayer debe ser el esperado.");
    }
}