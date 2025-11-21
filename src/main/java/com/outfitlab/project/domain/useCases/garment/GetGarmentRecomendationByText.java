package com.outfitlab.project.domain.useCases.garment;

import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.ConjuntoDTO;
import com.outfitlab.project.domain.model.dto.GeminiRecommendationDTO;
import com.outfitlab.project.domain.interfaces.port.GeminiClient;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;

import java.util.*;
import java.util.stream.Collectors;

public class GetGarmentRecomendationByText {

    private final GeminiClient geminiClient;
    private final GarmentRepository garmentRepository;
    public GetGarmentRecomendationByText(GeminiClient geminiClient, GarmentRepository garmentRepository) {
        this.geminiClient = geminiClient;
        this.garmentRepository = garmentRepository;
    }

    public List<ConjuntoDTO> execute(String peticionUsuario, String idUsuario) {
        GeminiRecommendationDTO params = geminiClient.extractParameters(peticionUsuario);

        String clima = params.getClima();
        String ocasion = params.getOcasion();

        List<PrendaModel> prendasAptas = garmentRepository.findByClimaAndOcasion(
                clima,
                ocasion
        );

        if (prendasAptas.isEmpty()) {
            return List.of(new ConjuntoDTO("Lo siento, no tengo prendas que coincidan con la búsqueda.", List.of()));
        }

        List<ConjuntoDTO> recomendaciones = generarConjuntos(prendasAptas);

        return recomendaciones;
    }

    private List<ConjuntoDTO> generarConjuntos(List<PrendaModel> prendasAptas) {

        Map<String, List<PrendaModel>> clasificado = prendasAptas.stream()
                .collect(Collectors.groupingBy(PrendaModel::getTipo));

        List<PrendaModel> tops = clasificado.getOrDefault("superior", List.of());
        List<PrendaModel> bottoms = clasificado.getOrDefault("inferior", List.of());

        if (tops.isEmpty() || bottoms.isEmpty()) {
            return List.of(new ConjuntoDTO("No hay suficientes prendas (superiores e inferiores) para armar conjuntos.", List.of()));
        }

        List<ConjuntoDTO> resultados = new ArrayList<>();
        Set<PrendaModel> prendasUsadasEnSets = new HashSet<>();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            PrendaModel top = seleccionarPrendaAleatoriaYNueva(tops, prendasUsadasEnSets, random);
            PrendaModel bottom = seleccionarPrendaAleatoriaYNueva(bottoms, prendasUsadasEnSets, random);

            if (top == null || bottom == null) {
                break;
            }

            if (sonCompatibles(top, bottom)) {
                List<PrendaModel> outfit = List.of(top, bottom);
                resultados.add(new ConjuntoDTO("Recomendación de Look #" + (i + 1), outfit));

                prendasUsadasEnSets.add(top);
                prendasUsadasEnSets.add(bottom);
            }
        }

        return resultados;
    }

    private PrendaModel seleccionarPrendaAleatoriaYNueva(
            List<PrendaModel> disponibles,
            Set<PrendaModel> excluidas,
            Random random
    ) {
        List<PrendaModel> candidatas = disponibles.stream()
                .filter(p -> !excluidas.contains(p))
                .collect(Collectors.toList());

        if (candidatas.isEmpty()) {
            return null;
        }

        int index = random.nextInt(candidatas.size());
        return candidatas.get(index);
    }

    private boolean sonCompatibles(PrendaModel top, PrendaModel bottom) {
        return true;
    }

}
