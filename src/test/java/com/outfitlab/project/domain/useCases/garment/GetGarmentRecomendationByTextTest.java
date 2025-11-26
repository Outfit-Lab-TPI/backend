package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.interfaces.port.GeminiClient;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.dto.ConjuntoDTO;
import com.outfitlab.project.domain.model.dto.GeminiRecommendationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetGarmentRecomendationByTextTest {

    private GeminiClient geminiClient;
    private GarmentRepository garmentRepository;
    private GetGarmentRecomendationByText useCase;

    @BeforeEach
    void setUp() {
        geminiClient = mock(GeminiClient.class);
        garmentRepository = mock(GarmentRepository.class);
        useCase = new GetGarmentRecomendationByText(geminiClient, garmentRepository);
    }

    @Test
    void debeRetornarMensajeCuandoNoHayPrendasAptas() {
        givenGeminiClientParameters("frio", "fiesta");
        givenNoPrendasAptas();

        List<ConjuntoDTO> resultado = whenEjecutaUseCase();

        thenDebeRetornarMensajeSinPrendas(resultado);
    }
    private void givenGeminiClientParameters(String clima, String ocasion) {
        GeminiRecommendationDTO dto = new GeminiRecommendationDTO(clima, ocasion);
        when(geminiClient.extractParameters(anyString())).thenReturn(dto);
    }

    private void givenNoPrendasAptas() {
        when(garmentRepository.findByClimaAndOcasion(anyString(), anyString()))
                .thenReturn(List.of());
    }

    private List<ConjuntoDTO> whenEjecutaUseCase() {
        return useCase.execute("hola", "usuario1");
    }

    private void thenDebeRetornarMensajeSinPrendas(List<ConjuntoDTO> resultado) {
        assertEquals(1, resultado.size());
        assertEquals("Lo siento, no tengo prendas que coincidan con la búsqueda.", resultado.get(0).getNombre());
        assertTrue(resultado.get(0).getPrendas().isEmpty());
    }
}
