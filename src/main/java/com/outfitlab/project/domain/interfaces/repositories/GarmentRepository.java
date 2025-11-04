package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.GarmentPageDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GarmentRepository {
    GarmentPageDTO findByBrandCodeAndTipo(String brandCode, String tipo, int page);
    Page<PrendaModel> getGarmentsByType(String type, int page);
    PrendaModel findByGarmentCode(String garmentCode) throws GarmentNotFoundException;
}
