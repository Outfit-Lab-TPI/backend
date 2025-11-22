package com.outfitlab.project.infrastructure.model;

import com.outfitlab.project.domain.model.ColorModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "color")
public class ColorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private int valor;

    public ColorEntity() {}

    public ColorEntity(String nombre, int valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public static ColorModel convertEntityToModel(ColorEntity entity) {
        return new ColorModel(
                entity.getId(),
                entity.getNombre(),
                entity.getValor()
        );
    }

    public static ColorEntity convertModelToEntity(ColorModel model) {
        return new ColorEntity(
                model.getNombre(),
                model.getValor()
        );
    }

}
