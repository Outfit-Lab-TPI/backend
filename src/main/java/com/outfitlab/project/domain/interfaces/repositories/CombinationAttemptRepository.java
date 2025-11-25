package com.outfitlab.project.domain.interfaces.repositories;
import com.outfitlab.project.domain.model.CombinationAttemptModel;
import com.outfitlab.project.domain.model.PrendaModel;

import java.util.List;

public interface CombinationAttemptRepository {

    Long save(CombinationAttemptModel attempt);

    List<CombinationAttemptModel> findAllByPrenda(Long prendaId);

    List<CombinationAttemptModel> findLastNDays(int i);

    List<CombinationAttemptModel> findAll();

    void deleteAllByAttempsReltedToCombinationRelatedToGarments(String garmentCode);
}