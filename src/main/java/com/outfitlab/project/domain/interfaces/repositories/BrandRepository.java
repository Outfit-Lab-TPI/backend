package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.BrandModel;
import java.util.List;

public interface BrandRepository {
    BrandModel findByBrandCode(String brandCode);// hay que hacer un DTO que no me deje traer el id
    List<BrandModel> getAllBrands(int page);
}
