package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.model.ColorModel;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.PageDTO;
import com.outfitlab.project.domain.model.ClimaModel;
import com.outfitlab.project.domain.model.OcasionModel;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface GarmentRepository {
    PageDTO findByBrandCodeAndTipo(String brandCode, String tipo, int page);
    Page<PrendaModel> getGarmentsByType(String type, int page);
    PrendaModel findByGarmentCode(String garmentCode) throws GarmentNotFoundException;

    void createGarment(
            String name,
            String type,
            String color,
            String brandCode,
            String imageUrl,
            String climaNombre,
            List<String> ocasionesNombres
    );

    @Transactional
    void deleteGarment(String garmentCode);

    @Transactional
    void updateGarment(String name, String type, String colorNombre, String event, String garmentCode, String imageUrl, String newGarmentCode, String climaNombre, List<String> ocasionesNombres);

    List<PrendaModel> findByClimaAndOcasion(String climaNombre, String ocasionNombre);

    List<ClimaModel> findAllClimas();

    List<OcasionModel> findAllOcasiones();

    List<ColorModel> findAllColores();
}
