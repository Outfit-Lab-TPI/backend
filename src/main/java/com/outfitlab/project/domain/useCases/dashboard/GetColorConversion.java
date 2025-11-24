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

    public List<ColorConversion> execute() {
        List<CombinationAttemptModel> attempts = combinationAttemptRepository.findAll();
        Map<String, Integer> colorMap = new HashMap<>();

        for (CombinationAttemptModel a : attempts) {
            // Color de la prenda superior
            String colorSup = a.getCombination().getPrendaSuperior().getColor().getNombre();
            colorMap.put(colorSup, colorMap.getOrDefault(colorSup, 0) + 1);

            // Color de la prenda inferior
            String colorInf = a.getCombination().getPrendaInferior().getColor().getNombre();
            colorMap.put(colorInf, colorMap.getOrDefault(colorInf, 0) + 1);
        }

        return colorMap.entrySet().stream()
                .map(e -> new ColorConversion(
                        e.getKey(),
                        e.getValue(),
                        0,      // favoritos siempre 0
                        0.0     // conversion siempre 0
                ))
                .sorted((a, b) -> Integer.compare(b.pruebas(), a.pruebas()))
                .collect(Collectors.toList());
    }
}
