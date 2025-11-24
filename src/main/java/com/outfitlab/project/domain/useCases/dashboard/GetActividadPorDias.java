package com.outfitlab.project.domain.useCases.dashboard;

import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import com.outfitlab.project.domain.model.dto.PrendaDashboardDTO.DiaPrueba;
import com.outfitlab.project.domain.model.CombinationAttemptModel;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetActividadPorDias {

    private final CombinationAttemptRepository combinationAttemptRepository;

    public GetActividadPorDias(CombinationAttemptRepository combinationAttemptRepository) {
        this.combinationAttemptRepository = combinationAttemptRepository;
    }

    public List<DiaPrueba> execute() {
        List<CombinationAttemptModel> attempts = combinationAttemptRepository.findLastNDays(30);
        int[] daily = new int[30];
        for (CombinationAttemptModel a : attempts) {
            int day = a.getCreatedAt().getDayOfMonth() - 1;
            if (day >= 0 && day < 30) daily[day]++;
        }
        List<DiaPrueba> res = new ArrayList<>();
        for(int i=0;i<30;i++) res.add(new DiaPrueba(i+1,daily[i]));
        return res;
    }
}
