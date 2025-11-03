package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.GarmentPageDTO;

import java.util.List;

public interface GarmentRepository {
    PrendaModel findByBrandCode(String brandCode, int page);
    List<PrendaModel> findAllByBrandCode(String brandCode, int page);
    GarmentPageDTO findByBrandCodeAndTipo(String brandCode, String tipo, int page);
}
