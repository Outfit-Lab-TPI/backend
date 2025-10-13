package com.outfitlab.project.infrastructure;

import com.outfitlab.project.domain.entities.Marca;
import com.outfitlab.project.domain.exceptions.MarcasNotFoundException;
import com.outfitlab.project.domain.repositories.MarcaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MarcaService {
    private final MarcaRepository marcaRepository;

    public List<Marca> getAllMarcas() throws MarcasNotFoundException {
        List<Marca> marcas = marcaRepository.findAll();
        System.out.println(marcas);
        if (!marcas.isEmpty()) {
            return marcas;
        }
        throw new MarcasNotFoundException("No encontramos marcas");
    }

    public Marca getMarcaAtributesByCodigoMarca(String codigoMarca) throws MarcasNotFoundException {
        Marca marca = marcaRepository.findByCodigoMarca(codigoMarca);
        if (marca != null) {
            return marca;
        }
        throw new MarcasNotFoundException("No encontramos la marca: " + codigoMarca);
    }
}
