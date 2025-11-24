package com.outfitlab.project.infrastructure.model;

import com.outfitlab.project.domain.model.ClimaModel;
import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "clima")
public class ClimaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public ClimaEntity() {}

    public ClimaEntity(String nombre) {
        this.nombre = nombre;
    }

    public static ClimaModel convertEntityToModel(ClimaEntity entity) {
        return new ClimaModel(entity.getId(), entity.getNombre());
    }
}