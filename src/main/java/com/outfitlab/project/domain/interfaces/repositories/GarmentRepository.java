package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.PageDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface GarmentRepository {
    PageDTO findByBrandCodeAndTipo(String brandCode, String tipo, int page);
    Page<PrendaModel> getGarmentsByType(String type, int page);
    PrendaModel findByGarmentCode(String garmentCode) throws GarmentNotFoundException;

    void createGarment(String name, String type, String color, String event, String brandCode, String imageUrl, String garmentCode);

    @Transactional
    void deleteGarment(String garmentCode);
}
