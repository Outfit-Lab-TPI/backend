package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.GarmentPageDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GarmentRepository {
    PrendaModel findByBrandCode(String brandCode, int page);
    List<PrendaModel> findAllByBrandCode(String brandCode, int page);
    GarmentPageDTO findByBrandCodeAndTipo(String brandCode, String tipo, int page);
    Page<PrendaModel> getGarmentsByType(String type, int page);
}
