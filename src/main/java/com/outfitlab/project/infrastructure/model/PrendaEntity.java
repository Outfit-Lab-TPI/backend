package com.outfitlab.project.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.outfitlab.project.domain.model.MarcaModel;
import com.outfitlab.project.domain.model.PrendaModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrendaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String imagenUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
    @JsonBackReference
    private MarcaEntity marca;

    public PrendaEntity(String nombre, MarcaEntity marca, String tipo, String imagenUrl) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.marca = marca;
    }

    // ------------- acá hacemos los dos convert ------------
    public static PrendaModel convertToModel(PrendaEntity prendaEntity) {
        return new PrendaModel(
                prendaEntity.getNombre(),
                prendaEntity.getMarca(),
                prendaEntity.getTipo(),
                prendaEntity.getImagenUrl()
        );
    }

    public static PrendaEntity convertToEntity(PrendaModel prendaModel) {
        return new PrendaEntity(
                prendaModel.getNombre(),
                prendaModel.getMarca(),
                prendaModel.getTipo(),
                prendaModel.getImagenUrl()
        );
    }
    //--------------------------------------------------------
}

