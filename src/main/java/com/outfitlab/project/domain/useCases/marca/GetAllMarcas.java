package com.outfitlab.project.domain.useCases.marca;

import com.outfitlab.project.domain.exceptions.MarcasNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.MarcaRepository;
import com.outfitlab.project.domain.model.MarcaModel;

import java.util.List;

public class GetAllMarcas {

    private final MarcaRepository iMarcaRepository;

    public GetAllMarcas(MarcaRepository marcaRepository) {
        this.iMarcaRepository = marcaRepository;
    }

    public List<MarcaModel> execute() throws MarcasNotFoundException {
        List<MarcaModel> marcas = iMarcaRepository.obtenerTodas();
        System.out.println(" -------------------------------" + marcas);
        if (!marcas.isEmpty()) {
            return marcas;
        }
        throw new MarcasNotFoundException("No encontramos marcas");
    }
}
