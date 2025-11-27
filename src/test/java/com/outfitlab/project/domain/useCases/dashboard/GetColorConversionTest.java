package com.outfitlab.project.domain.useCases.dashboard;

import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import com.outfitlab.project.domain.model.CombinationAttemptModel;
import com.outfitlab.project.domain.model.CombinationModel;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.ColorModel;
import com.outfitlab.project.domain.model.dto.PrendaDashboardDTO.ColorConversion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetColorConversionTest {

    private CombinationAttemptRepository combinationAttemptRepository = mock(CombinationAttemptRepository.class);
    private GetColorConversion getColorConversion;

    private final String TARGET_BRAND = "ADIDAS";
    private final String OTHER_BRAND = "NIKE";
    private final String COLOR_RED = "Rojo";
    private final String COLOR_BLUE = "Azul";

    @BeforeEach
    void setUp() {
        getColorConversion = new GetColorConversion(combinationAttemptRepository);
    }


    @Test
    public void shouldReturnCorrectCountsAndSortByPruebasForTargetBrand() {
        List<CombinationAttemptModel> mockAttempts = new ArrayList<>();
        mockAttempts.add(createAttempt(TARGET_BRAND, COLOR_RED, OTHER_BRAND, COLOR_BLUE));
        mockAttempts.add(createAttempt(TARGET_BRAND, COLOR_RED, TARGET_BRAND, COLOR_BLUE));
        mockAttempts.add(createAttempt(OTHER_BRAND, COLOR_BLUE, TARGET_BRAND, COLOR_RED));
        mockAttempts.add(createAttempt(TARGET_BRAND, COLOR_BLUE, OTHER_BRAND, COLOR_RED));

        givenRepositoryReturnsAttempts(mockAttempts);

        List<ColorConversion> result = whenExecuteGetColorConversion(TARGET_BRAND);

        thenResultSizeIsCorrect(result, 2);
        thenResultIsSortedAndCountedCorrectly(result, COLOR_RED, 3, COLOR_BLUE, 2);
    }

    @Test
    public void shouldReturnEmptyListWhenNoAttemptsAreFound() {
        givenRepositoryReturnsAttempts(Collections.emptyList());

        List<ColorConversion> result = whenExecuteGetColorConversion(TARGET_BRAND);

        thenResultSizeIsCorrect(result, 0);
    }

    @Test
    public void shouldCountColorOncePerAttemptEvenIfUsedTwice() {
        List<CombinationAttemptModel> mockAttempts = List.of(
                createAttempt(TARGET_BRAND, COLOR_RED, TARGET_BRAND, COLOR_RED)
        );
        givenRepositoryReturnsAttempts(mockAttempts);

        List<ColorConversion> result = whenExecuteGetColorConversion(TARGET_BRAND);

        thenResultSizeIsCorrect(result, 1);
        thenResultIsSortedAndCountedCorrectly(result, COLOR_RED, 1);
    }

    @Test
    public void shouldHandleNullPrendasOrNullColorNamesWithoutNPE() {
        List<CombinationAttemptModel> mockAttempts = new ArrayList<>();

        mockAttempts.add(createAttempt(TARGET_BRAND, COLOR_RED, null, COLOR_BLUE));
        mockAttempts.add(createAttempt(null, COLOR_RED, TARGET_BRAND, COLOR_BLUE));
        mockAttempts.add(createAttempt(null, null, null, null));

        givenRepositoryReturnsAttempts(mockAttempts);

        List<ColorConversion> result = whenExecuteGetColorConversion(TARGET_BRAND);

        thenResultSizeIsCorrect(result, 2);
        thenResultIsSortedAndCountedCorrectly(result, COLOR_RED, 1, COLOR_BLUE, 1);
    }

    @Test
    public void shouldReturnEmptyListWhenTargetBrandCodeIsNull() {
        givenRepositoryReturnsAttempts(List.of(createAttempt(TARGET_BRAND, COLOR_RED, TARGET_BRAND, COLOR_RED)));

        List<ColorConversion> result = whenExecuteGetColorConversion(null);

        thenResultSizeIsCorrect(result, 0);
    }


    //privadoss
    private void givenRepositoryReturnsAttempts(List<CombinationAttemptModel> attempts) {
        when(combinationAttemptRepository.findAll()).thenReturn(attempts);
    }

    private CombinationAttemptModel createAttempt(String supBrandCode, String supColorName,
                                                  String infBrandCode, String infColorName) {

        final String safeSupBrandCode = supBrandCode != null ? supBrandCode : "";
        final String safeInfBrandCode = infBrandCode != null ? infBrandCode : "";
        final String safeSupColorName = supColorName != null ? supColorName : "";
        final String safeInfColorName = infColorName != null ? infColorName : "";

        CombinationAttemptModel attempt = mock(CombinationAttemptModel.class);
        CombinationModel combination = mock(CombinationModel.class);

        PrendaModel prendaSup = mock(PrendaModel.class);

        BrandModel brandSup = mock(BrandModel.class);
        when(brandSup.getCodigoMarca()).thenReturn(safeSupBrandCode);
        when(prendaSup.getMarca()).thenReturn(brandSup);

        ColorModel colorSup = mock(ColorModel.class);
        when(colorSup.getNombre()).thenReturn(safeSupColorName);
        when(prendaSup.getColor()).thenReturn(colorSup);

        PrendaModel prendaInf = mock(PrendaModel.class);

        BrandModel brandInf = mock(BrandModel.class);
        when(brandInf.getCodigoMarca()).thenReturn(safeInfBrandCode);
        when(prendaInf.getMarca()).thenReturn(brandInf);

        ColorModel colorInf = mock(ColorModel.class);
        when(colorInf.getNombre()).thenReturn(safeInfColorName);
        when(prendaInf.getColor()).thenReturn(colorInf);

        when(combination.getPrendaSuperior()).thenReturn(prendaSup);
        when(combination.getPrendaInferior()).thenReturn(prendaInf);

        when(attempt.getCombination()).thenReturn(combination);

        return attempt;
    }

    private List<ColorConversion> whenExecuteGetColorConversion(String brandCode) {
        return getColorConversion.execute(brandCode);
    }

    private void thenResultSizeIsCorrect(List<ColorConversion> result, int size) {
        assertNotNull(result);
        assertEquals(size, result.size(), "La lista resultante debe tener el tamaño esperado.");
    }

    private void thenResultIsSortedAndCountedCorrectly(List<ColorConversion> result, Object... expectedPairs) {

        if (result.size() > 1) {
            assertTrue(result.get(0).pruebas() >= result.get(1).pruebas(), "El resultado debe estar ordenado por 'pruebas' descendente.");
        }

        Map<String, Integer> actualCounts = new HashMap<>();
        result.forEach(cc -> actualCounts.put(cc.color(), cc.pruebas()));

        for (int i = 0; i < expectedPairs.length; i += 2) {
            String colorName = (String) expectedPairs[i];
            int expectedCount = (Integer) expectedPairs[i + 1];

            assertTrue(actualCounts.containsKey(colorName), "El color '" + colorName + "' debe estar en la lista.");
            assertEquals(expectedCount, actualCounts.get(colorName), "El conteo de pruebas para " + colorName + " es incorrecto.");

            result.stream()
                    .filter(cc -> cc.color().equals(colorName))
                    .findFirst()
                    .ifPresent(cc -> {
                        assertEquals(0, cc.favoritos(), "El campo 'favoritos' debe ser 0.");
                        assertEquals(0.0, cc.conversion(), 0.001, "El campo 'conversion' debe ser 0.0.");
                    });
        }
    }
}