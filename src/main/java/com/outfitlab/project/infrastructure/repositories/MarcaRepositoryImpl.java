package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.MarcaRepository;
import com.outfitlab.project.domain.model.MarcaModel;
import com.outfitlab.project.infrastructure.model.MarcaEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.MarcaJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MarcaRepositoryImpl implements MarcaRepository {

    private final MarcaJpaRepository jpaMarcaRepository;

    public MarcaRepositoryImpl(MarcaJpaRepository marcaRepository) {
        this.jpaMarcaRepository = marcaRepository;
    }

    @Override
    public MarcaModel buscarPorCodigoMarca(String codigoMarca) {
        return MarcaEntity.convertToModel(this.jpaMarcaRepository.findByCodigoMarca(codigoMarca));
    }

    @Override
    public List<MarcaModel> obtenerTodas() {
        return this.jpaMarcaRepository.findAll()
                .stream()
                .map(MarcaEntity::convertToModel)
                .collect(Collectors.toList());
    }
}
