package com.outfitlab.project.domain.useCases.marca;

import com.outfitlab.project.domain.exceptions.MarcasNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.MarcaRepository;
import com.outfitlab.project.domain.model.MarcaModel;

public class GetMarcaByCodigoMarca {

    private MarcaRepository marcaRepository;

    public GetMarcaByCodigoMarca(MarcaRepository marcaRepository) {
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
