package com.outfitlab.project.domain.useCases.dashboard;

import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import com.outfitlab.project.domain.model.CombinationAttemptModel;
import com.outfitlab.project.domain.model.CombinationModel;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.domain.model.dto.PrendaDashboardDTO.ComboPopular;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetTopCombosTest {

    private CombinationAttemptRepository combinationAttemptRepository = mock(CombinationAttemptRepository.class);
    private GetTopCombos getTopCombos;

    private final String TARGET_BRAND = "ADIDAS";
    private final String OTHER_BRAND = "NIKE";
    private final String P_REMERA = "Remera";
    private final String P_JEANS = "Jeans";
    private final String P_BUZO = "Buzo";
    private final String P_SHORTS = "Shorts";
    private final String IMG_SUP = "url/sup";
    private final String IMG_INF = "url/inf";
    private final int TOP_N_LIMIT = 2;

    @BeforeEach
    void setUp() {
        getTopCombos = new GetTopCombos(combinationAttemptRepository);
    }


    @Test
    public void shouldGroupCountAndSortTopNCombosByPruebas() {
        List<CombinationAttemptModel> mockAttempts = new ArrayList<>();

        //combo 1
        mockAttempts.add(createAttempt(P_REMERA, P_JEANS, TARGET_BRAND, TARGET_BRAND, true));
        mockAttempts.add(createAttempt(P_REMERA, P_JEANS, TARGET_BRAND, OTHER_BRAND, true));
        mockAttempts.add(createAttempt(P_REMERA, P_JEANS, OTHER_BRAND, TARGET_BRAND, false));

        //combo 2
        mockAttempts.add(createAttempt(P_BUZO, P_SHORTS, TARGET_BRAND, TARGET_BRAND, true));

        //combo 3
        mockAttempts.add(createAttempt(P_REMERA, P_SHORTS, TARGET_BRAND, TARGET_BRAND, true));
        mockAttempts.add(createAttempt(P_REMERA, P_SHORTS, TARGET_BRAND, TARGET_BRAND, true));
        mockAttempts.add(createAttempt(P_REMERA, P_SHORTS, TARGET_BRAND, TARGET_BRAND, true));
        mockAttempts.add(createAttempt(P_REMERA, P_SHORTS, TARGET_BRAND, OTHER_BRAND, true));

        //combo ignorafo
        mockAttempts.add(createAttempt(P_BUZO, P_JEANS, OTHER_BRAND, OTHER_BRAND, true));
        mockAttempts.add(createAttempt(P_BUZO, P_JEANS, OTHER_BRAND, OTHER_BRAND, true));

        givenRepositoryReturnsAttempts(mockAttempts);

        List<ComboPopular> result = whenExecuteGetTopCombos(TOP_N_LIMIT, TARGET_BRAND);

        thenResultSizeIsCorrect(result, TOP_N_LIMIT);
        thenResultIsCorrectlySorted(result, P_REMERA, P_SHORTS, P_REMERA, P_JEANS);
        thenComboHasCorrectCounts(result.get(0), P_REMERA, P_SHORTS, 4, 4);
    }

    @Test
    public void shouldReturnEmptyListWhenNoAttemptsAreFound() {
        givenRepositoryReturnsAttempts(Collections.emptyList());

        List<ComboPopular> result = whenExecuteGetTopCombos(TOP_N_LIMIT, TARGET_BRAND);

        thenResultSizeIsCorrect(result, 0);
    }

    @Test
    public void shouldReturnEmptyListWhenOnlyOtherBrandsArePresent() {
        List<CombinationAttemptModel> mockAttempts = List.of(
                createAttempt(P_REMERA, P_JEANS, OTHER_BRAND, OTHER_BRAND, true)
        );
        givenRepositoryReturnsAttempts(mockAttempts);

        List<ComboPopular> result = whenExecuteGetTopCombos(TOP_N_LIMIT, TARGET_BRAND);

        thenResultSizeIsCorrect(result, 0);
    }

    @Test
    public void shouldIgnoreComboIfBothBrandsAreNull() {
        List<CombinationAttemptModel> mockAttempts = List.of(
                createAttempt(P_REMERA, P_JEANS, null, null, true)
        );
        givenRepositoryReturnsAttempts(mockAttempts);

        List<ComboPopular> result = whenExecuteGetTopCombos(TOP_N_LIMIT, TARGET_BRAND);

        thenResultSizeIsCorrect(result, 0);
    }

    @Test
    public void shouldCountThumbsAsZeroWhenUserModelIsNull() {
        List<CombinationAttemptModel> mockAttempts = List.of(
                createAttempt(P_REMERA, P_JEANS, TARGET_BRAND, OTHER_BRAND, true),
                createAttempt(P_REMERA, P_JEANS, TARGET_BRAND, OTHER_BRAND, false)
        );
        givenRepositoryReturnsAttempts(mockAttempts);

        List<ComboPopular> result = whenExecuteGetTopCombos(TOP_N_LIMIT, TARGET_BRAND);

        thenResultSizeIsCorrect(result, 1);
        thenComboHasCorrectCounts(result.get(0), P_REMERA, P_JEANS, 2, 1);
    }


    //privadoss
    private void givenRepositoryReturnsAttempts(List<CombinationAttemptModel> attempts) {
        when(combinationAttemptRepository.findAll()).thenReturn(attempts);
    }

    private CombinationAttemptModel createAttempt(String supName, String infName,
                                                  String supBrandCode, String infBrandCode,
                                                  boolean userExists) {

        final String safeSupBrandCode = supBrandCode != null ? supBrandCode : "";
        final String safeInfBrandCode = infBrandCode != null ? infBrandCode : "";

        CombinationAttemptModel attempt = mock(CombinationAttemptModel.class);
        CombinationModel combination = mock(CombinationModel.class);
        PrendaModel prendaSup = mock(PrendaModel.class);
        PrendaModel prendaInf = mock(PrendaModel.class);

        BrandModel mockBrandSup = mock(BrandModel.class);
        when(mockBrandSup.getCodigoMarca()).thenReturn(safeSupBrandCode);
        when(prendaSup.getMarca()).thenReturn(mockBrandSup);

        BrandModel mockBrandInf = mock(BrandModel.class);
        when(mockBrandInf.getCodigoMarca()).thenReturn(safeInfBrandCode);
        when(prendaInf.getMarca()).thenReturn(mockBrandInf);

        when(prendaSup.getNombre()).thenReturn(supName);
        when(prendaInf.getNombre()).thenReturn(infName);
        when(prendaSup.getImagenUrl()).thenReturn(IMG_SUP);
        when(prendaInf.getImagenUrl()).thenReturn(IMG_INF);

        when(combination.getPrendaSuperior()).thenReturn(prendaSup);
        when(combination.getPrendaInferior()).thenReturn(prendaInf);

        UserModel mockUser = userExists ? mock(UserModel.class) : null;
        when(attempt.getUser()).thenReturn(mockUser);

        when(attempt.getCombination()).thenReturn(combination);

        return attempt;
    }

    private List<ComboPopular> whenExecuteGetTopCombos(int topN, String brandCode) {
        return getTopCombos.execute(topN, brandCode);
    }

    private void thenResultSizeIsCorrect(List<ComboPopular> result, int size) {
        assertNotNull(result);
        assertEquals(size, result.size(), "La lista resultante debe tener el tamaño TOP_N esperado.");
    }

    private void thenResultIsCorrectlySorted(List<ComboPopular> result, String top1Sup, String top1Inf, String top2Sup, String top2Inf) {
        assertEquals(top1Sup, result.get(0).getSuperior(), "El Top 1 debe tener la prenda superior correcta.");
        assertEquals(top1Inf, result.get(0).getInferior(), "El Top 1 debe tener la prenda inferior correcta.");

        assertTrue(result.get(0).getPruebas() >= result.get(1).getPruebas(), "El Top 1 debe tener más pruebas que el Top 2.");

        assertEquals(top2Sup, result.get(1).getSuperior(), "El Top 2 debe tener la prenda superior correcta.");
        assertEquals(top2Inf, result.get(1).getInferior(), "El Top 2 debe tener la prenda inferior correcta.");
    }

    private void thenComboHasCorrectCounts(ComboPopular combo, String supName, String infName, int expectedPruebas, int expectedThumbs) {
        assertEquals(supName, combo.getSuperior());
        assertEquals(infName, combo.getInferior());
        assertEquals(expectedPruebas, combo.getPruebas(), "El conteo de Pruebas es incorrecto.");
        assertEquals(expectedThumbs, combo.getThumbs(), "El conteo de Thumbs (Likes) es incorrecto.");
    }
}