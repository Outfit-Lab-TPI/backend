package com.outfitlab.project.domain.service;

import com.outfitlab.project.domain.model.MarcaModel;
import com.outfitlab.project.infrastructure.model.MarcaEntity;
import com.outfitlab.project.domain.exceptions.MarcasNotFoundException;
import com.outfitlab.project.domain.useCases.marca.GetAllMarcas;
import com.outfitlab.project.domain.useCases.marca.GetMarcaByCodigoMarca;
import java.util.List;

public class MarcaService {

    private final GetAllMarcas getAllMarcas;
    private final GetMarcaByCodigoMarca getMarcaByCodigoMarca;

    public MarcaService(GetAllMarcas getAllMarcas, GetMarcaByCodigoMarca getMarcaAtributesByCodigoMarca){
        this.getAllMarcas = getAllMarcas;
        this.getMarcaByCodigoMarca = getMarcaAtributesByCodigoMarca;
    }

    public List<MarcaModel> getAllMarcas() throws MarcasNotFoundException {
        try {
            return this.getAllMarcas.execute();
        }catch (Exception e){
            throw new MarcasNotFoundException(e.getMessage());
        }
    }

    public MarcaModel getMarcaByCodigoMarca(String codigoMarca) throws MarcasNotFoundException {
        try{
            return this.getMarcaByCodigoMarca.execute(codigoMarca);
        }catch (Exception e){
            throw new MarcasNotFoundException("No encontramos la marca: " + codigoMarca);
        }
    }
}
