package com.outfitlab.project.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.ClimaModel;
import com.outfitlab.project.domain.model.OcasionModel;
import com.outfitlab.project.domain.model.PrendaModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Column(nullable = false)
    private String color;

    @Column(unique = true)
    private String garmentCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
    @JsonBackReference
    private MarcaEntity marca;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clima_id", nullable = false)
    private ClimaEntity climaAdecuado;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "prenda_ocasion",
            joinColumns = @JoinColumn(name = "prenda_id"),
            inverseJoinColumns = @JoinColumn(name = "ocasion_id")
    )
    private Set<OcasionEntity> ocasiones;

    public PrendaEntity(){}

    public PrendaEntity(String nombre, MarcaEntity marca, String tipo, String imagenUrl, String garmentCode, String color, ClimaEntity climaAdecuado, Set<OcasionEntity> ocasiones) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.garmentCode = garmentCode;
        this.color = color;
        this.marca = marca;
        this.climaAdecuado = climaAdecuado;
        this.ocasiones = ocasiones;
    }

    public PrendaEntity(String nombre, MarcaEntity marca, String tipo, String imagenUrl, String garmentCode) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.marca = marca;
        this.garmentCode = garmentCode;
    }

    public static PrendaModel convertToModel(PrendaEntity prendaEntity) {
        BrandModel marcaModel = MarcaEntity.convertToModelWithoutPrendas(prendaEntity.getMarca());

        ClimaModel climaModel = new ClimaModel(
                prendaEntity.getClimaAdecuado().getId(),
                prendaEntity.getClimaAdecuado().getNombre()
        );

        Set<OcasionModel> ocasionesModels = prendaEntity.getOcasiones().stream()
                .map(ocasionEntity -> new OcasionModel(ocasionEntity.getId(), ocasionEntity.getNombre()))
                .collect(Collectors.toSet());

        return new PrendaModel(
                prendaEntity.getNombre(),
                marcaModel,
                prendaEntity.getTipo(),
                prendaEntity.getImagenUrl(),
                prendaEntity.getGarmentCode(),
                prendaEntity.getColor(),
                climaModel,
                ocasionesModels
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

    public static List<PrendaModel> convertToListModel(List<PrendaEntity> garments) {
        return  garments.stream().map(PrendaEntity::convertToModel)
                .toList();
    }
}