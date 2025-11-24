package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.ClimaModel;

import java.util.List;

public interface ClimaRepository {
    List<ClimaModel> findAllClimas();
}
