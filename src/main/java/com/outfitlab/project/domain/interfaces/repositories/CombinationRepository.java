package com.outfitlab.project.domain.interfaces.repositories;
import com.outfitlab.project.domain.model.CombinationModel;

import java.util.Optional;

public interface CombinationRepository {

    Optional<CombinationModel> findById(Long id);

    Optional<CombinationModel> findByPrendas(Long prendaSuperiorId, Long prendaInferiorId);

    CombinationModel save(CombinationModel combinationModel);

    void deleteAllByGarmentcode(String garmentCode);
}