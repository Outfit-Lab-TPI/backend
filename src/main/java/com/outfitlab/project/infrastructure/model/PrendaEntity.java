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
@AllArgsConstructor
@Table(name = "prenda")
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

    @Column(unique = true)
    private String garmentCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
    @JsonBackReference
    private MarcaEntity marca;

    public PrendaEntity(){}

    public PrendaEntity(String nombre, MarcaEntity marca, String tipo, String imagenUrl, String garmentCode) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.marca = marca;
        this.garmentCode = garmentCode;
    }

    // ------------- acá hacemos los dos convert ------------


    public static PrendaModel convertToModel(PrendaEntity prendaEntity) {
        MarcaModel marcaModel = MarcaEntity.convertToModelWithoutPrendas(prendaEntity.getMarca());
        return new PrendaModel(
                prendaEntity.getNombre(),
                marcaModel,
                prendaEntity.getTipo(),
                prendaEntity.getImagenUrl(),
                prendaEntity.getGarmentCode()
        );
    }

    public static PrendaEntity convertToEntity(PrendaModel prendaModel) {
        MarcaEntity entityMarca = MarcaEntity.convertToEntityWithoutPrendas(prendaModel.getMarca());
        return new PrendaEntity(
                prendaModel.getNombre(),
                entityMarca,
                prendaModel.getTipo(),
                prendaModel.getImagenUrl(),
                prendaModel.getGarmentCode()
        );
    }
    //--------------------------------------------------------
}

