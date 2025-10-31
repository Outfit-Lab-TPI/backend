package com.outfitlab.project.domain.useCases.marca;

import com.outfitlab.project.domain.exceptions.MarcasNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.IMarcaRepository;
import com.outfitlab.project.domain.model.MarcaModel;
import com.outfitlab.project.infrastructure.model.MarcaEntity;

public class GetMarcaByCodigoMarca {

    private IMarcaRepository marcaRepository;

    public GetMarcaByCodigoMarca(IMarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    public MarcaModel execute(String codigoMarca) throws MarcasNotFoundException {
        MarcaModel marca = marcaRepository.buscarPorCodigoMarca(codigoMarca);
        if (marca != null) {
            return marca;
        }
        throw new MarcasNotFoundException("No encontramos la marca: " + codigoMarca);
    }
}
