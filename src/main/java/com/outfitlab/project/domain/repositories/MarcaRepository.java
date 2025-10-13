package com.outfitlab.project.domain.repositories;

import com.outfitlab.project.domain.entities.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    Marca findByCodigoMarca(String codigoMarca);// hay que hacer un DTO que no me deje traer el id

}
