package com.outfitlab.project.domain.useCases.fashn;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.interfaces.repositories.FashnRepository;
import com.outfitlab.project.domain.model.dto.CombineRequestDTO;
import com.outfitlab.project.infrastructure.repositories.FashnRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CombinePrendasTest {

    private FashnRepository fashnRepository = mock(FashnRepositoryImpl.class);

    private CombinePrendas combinePrendas = new CombinePrendas(fashnRepository);

    @Test
    public void givenNullTopAndBottomWhenExecuteThenThrowFashnApiException() {
        CombineRequestDTO request = new CombineRequestDTO(null, null, false, "male");

        assertThrows(FashnApiException.class, () -> combinePrendas.execute(request));
    }

    @Test
    public void givenOnlyTopWhenExecuteThenCallCombineWithTopsCategory() throws Exception {
        CombineRequestDTO request = new CombineRequestDTO("top-url", null, false, "female");

        when(fashnRepository.combine("top-url", "tops", "female")).thenReturn("taskId123");
        when(fashnRepository.pollStatus("taskId123")).thenReturn("result-top-ok");

        String result = combinePrendas.execute(request);

        assertNotNull(result);
        assertEquals("result-top-ok", result);
    }

    @Test
    public void givenOnlyBottomWhenExecuteThenCallCombineWithBottomsCategory() throws Exception {
        CombineRequestDTO request = new CombineRequestDTO(null, "bottom-url",false, "female");

        when(fashnRepository.combine("bottom-url", "bottoms", "female")).thenReturn("taskId456");
        when(fashnRepository.pollStatus("taskId456")).thenReturn("result-bottom-ok");

        String result = combinePrendas.execute(request);

        assertNotNull(result);
        assertEquals("result-bottom-ok", result);
    }

    @Test
    public void givenTopAndBottomWhenExecuteThenCallCombineTopAndBottom() throws Exception {
        CombineRequestDTO request = new CombineRequestDTO("top-url", "bottom-url", false, "male");

        when(fashnRepository.combineTopAndBottom("top-url", "bottom-url", "male"))
                .thenReturn("combined-result");

        String result = combinePrendas.execute(request);

        assertNotNull(result);
        assertEquals("combined-result", result);
    }
}