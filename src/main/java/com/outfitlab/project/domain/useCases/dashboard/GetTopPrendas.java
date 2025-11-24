package com.outfitlab.project.domain.useCases.dashboard;

import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.dto.PrendaDashboardDTO.TopPrenda;
import com.outfitlab.project.domain.model.dto.PrendaDashboardDTO.DailyPrueba;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.CombinationAttemptModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetTopPrendas {

    private final GarmentRepository prendaRepository;
    private final CombinationAttemptRepository combinationAttemptRepository;

    public GetTopPrendas(GarmentRepository prendaRepository,
                         CombinationAttemptRepository combinationAttemptRepository) {
        this.prendaRepository = prendaRepository;
        this.combinationAttemptRepository = combinationAttemptRepository;
    }

    public List<TopPrenda> execute(int topN, String brandCode) {
        List<PrendaModel> prendas = prendaRepository.findAll();
        Map<Long, TopPrenda> map = new HashMap<>();

        LocalDate today = LocalDate.now();

        for (PrendaModel p : prendas) {
            if (!p.getMarca().getCodigoMarca().equals(brandCode)) continue;

            List<CombinationAttemptModel> attempts = combinationAttemptRepository.findAllByPrenda(p.getId());

            int pruebas = attempts.size();

            // Construir daily para últimos 30 días
            List<DailyPrueba> daily = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                LocalDate day = today.minusDays(29 - i);
                int count = (int) attempts.stream()
                        .filter(a -> a.getCreatedAt().toLocalDate().isEqual(day))
                        .count();
                daily.add(new DailyPrueba(i + 1, count));
            }

            map.put(p.getId(), new TopPrenda(
                    p.getId(),
                    p.getNombre(),
                    p.getColor().getNombre(),
                    p.getImagenUrl(),
                    pruebas,
                    daily
            ));
        }

        return map.values().stream()
                .sorted((a, b) -> Integer.compare(b.pruebas(), a.pruebas()))
                .limit(topN)
                .collect(Collectors.toList());
    }
}
