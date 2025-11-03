package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.MarcaModel;
import java.util.List;

public interface MarcaRepository {
    MarcaModel buscarPorCodigoMarca(String codigoMarca);// hay que hacer un DTO que no me deje traer el id
    List<MarcaModel> obtenerTodas();
}
