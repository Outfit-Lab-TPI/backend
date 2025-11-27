package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.exceptions.ColorNotFoundException;
import com.outfitlab.project.domain.exceptions.OcasionNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.ColorRepository;
import com.outfitlab.project.domain.model.ColorModel;
import com.outfitlab.project.infrastructure.model.ColorEntity;
import com.outfitlab.project.infrastructure.model.OcasionEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.ColorJpaRepository;

import java.util.List;

public class ColorRepositoryImpl implements ColorRepository {

    private final ColorJpaRepository colorJpaRepository;

    public ColorRepositoryImpl(ColorJpaRepository colorJpaRepository) {
        this.colorJpaRepository = colorJpaRepository;
    }

    @Override
    public List<ColorModel> findAllColores() {
        List<ColorEntity> colors = this.colorJpaRepository.findAll();
        if (colors.isEmpty()) throw new ColorNotFoundException("No encontramos ocasiones.");
        return colors.stream().map(ColorEntity::convertEntityToModel)
                .toList();
    }
}
