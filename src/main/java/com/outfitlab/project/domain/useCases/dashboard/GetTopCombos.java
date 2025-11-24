package com.outfitlab.project.domain.useCases.dashboard;

import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.dto.PrendaDashboardDTO.ComboPopular;
import com.outfitlab.project.domain.model.CombinationAttemptModel;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetTopCombos {

    private final CombinationAttemptRepository combinationAttemptRepository;

    public GetTopCombos(CombinationAttemptRepository combinationAttemptRepository) {
        this.combinationAttemptRepository = combinationAttemptRepository;
    }

    public List<ComboPopular> execute(int topN, String brandCode) {
        List<CombinationAttemptModel> attempts = combinationAttemptRepository.findAll();
        Map<String, ComboPopular> map = new HashMap<>();

        for (CombinationAttemptModel a : attempts) {
            BrandModel supMarca = a.getCombination().getPrendaSuperior().getMarca();
            BrandModel infMarca = a.getCombination().getPrendaInferior().getMarca();

            String supBrand = supMarca != null ? supMarca.getCodigoMarca() : "";
            String infBrand = infMarca != null ? infMarca.getCodigoMarca() : "";

// Solo procesar si al menos una prenda coincide con la marca
            if (!supBrand.equals(brandCode) && !infBrand.equals(brandCode)) continue;


            String key = a.getCombination().getPrendaSuperior().getNombre() + "_" +
                    a.getCombination().getPrendaInferior().getNombre();

            map.putIfAbsent(key, new ComboPopular(
                    a.getCombination().getPrendaSuperior().getNombre(),
                    a.getCombination().getPrendaInferior().getNombre(),
                    a.getCombination().getPrendaSuperior().getImagenUrl(),
                    a.getCombination().getPrendaInferior().getImagenUrl(),
                    0, 0
            ));

            ComboPopular c = map.get(key);
            c.setPruebas(c.getPruebas() + 1);
            c.setThumbs(c.getThumbs() + (a.getUser() != null ? 1 : 0));
        }

        return map.values().stream()
                .sorted((a, b) -> Integer.compare(b.getPruebas(), a.getPruebas()))
                .limit(topN)
                .collect(Collectors.toList());
    }
}
