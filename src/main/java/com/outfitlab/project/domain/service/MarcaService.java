package com.outfitlab.project.domain.service;

import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.useCases.marca.GetAllBrands;
import com.outfitlab.project.domain.useCases.marca.GetBrandAndGarmentsByBrandCode;
import java.util.List;

public class MarcaService {

    private final GetAllBrands getAllMarcas;
    private final GetBrandAndGarmentsByBrandCode getMarcaByCodigoMarca;

    public MarcaService(GetAllBrands getAllMarcas, GetBrandAndGarmentsByBrandCode getMarcaAtributesByCodigoMarca){
        this.getAllMarcas = getAllMarcas;
        this.getMarcaByCodigoMarca = getMarcaAtributesByCodigoMarca;
    }

    public List<BrandModel> getAllMarcas() throws BrandsNotFoundException {
        try {
            //return this.getAllMarcas.execute(1);
            return  null;
        }catch (Exception e){
            throw new BrandsNotFoundException(e.getMessage());
        }
    }

    public BrandModel getMarcaByCodigoMarca(String codigoMarca) throws BrandsNotFoundException {
       return null;
    }
}
