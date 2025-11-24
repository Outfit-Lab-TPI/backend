package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.OcasionModel;

import java.util.List;

public interface OcacionRepository {
    List<OcasionModel> findAllOcasiones();
}
