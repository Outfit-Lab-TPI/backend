package com.outfitlab.project.domain.useCases.dashboard;

import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import com.outfitlab.project.domain.model.dto.PrendaDashboardDTO.ColorConversion;
import com.outfitlab.project.domain.model.CombinationAttemptModel;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetColorConversion {

    private final CombinationAttemptRepository combinationAttemptRepository;

    public GetColorConversion(CombinationAttemptRepository combinationAttemptRepository) {
        this.combinationAttemptRepository = combinationAttemptRepository;
    }

    public List<ColorConversion> execute(String brandCode) {
        List<CombinationAttemptModel> attempts = combinationAttemptRepository.findAll();
        Map<String, Integer> colorMap = new HashMap<>();

        for (CombinationAttemptModel a : attempts) {
            Set<String> colores = new HashSet<>();

            if (a.getCombination().getPrendaSuperior().getMarca().getCodigoMarca().equals(brandCode)) {
                colores.add(a.getCombination().getPrendaSuperior().getColor().getNombre());
            }

            if (a.getCombination().getPrendaInferior().getMarca().getCodigoMarca().equals(brandCode)) {
                colores.add(a.getCombination().getPrendaInferior().getColor().getNombre());
            }

            for (String color : colores) {
                colorMap.put(color, colorMap.getOrDefault(color, 0) + 1);
            }
        }

        return colorMap.entrySet().stream()
                .map(e -> new ColorConversion(
                        e.getKey(),
                        e.getValue(),
                        0,      // favoritos siempre 0
                        0.0     // conversion siempre 0
                ))
                .sorted((a, b) -> Integer.compare(b.pruebas(), a.pruebas()))
                .toList();
    }

}
