package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.ColorModel;

import java.util.List;

public interface ColorRepository {
    List<ColorModel> findAllColores();
}
