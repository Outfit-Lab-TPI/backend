package com.outfitlab.project.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.outfitlab.project.domain.model.*;
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

    @Column(unique = true)
    private String garmentCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
    @JsonBackReference
    private MarcaEntity marca;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clima_id", nullable = false)
    private ClimaEntity climaAdecuado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "color_id", nullable = false)
    private ColorEntity color;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "prenda_ocasion",
            joinColumns = @JoinColumn(name = "prenda_id"),
            inverseJoinColumns = @JoinColumn(name = "ocasion_id")
    )
    private Set<OcasionEntity> ocasiones;

    private String genero = "hombre";

    public PrendaEntity(){}

    public PrendaEntity(String nombre, MarcaEntity marca, String tipo, String imagenUrl, String garmentCode, ColorEntity color, ClimaEntity climaAdecuado, Set<OcasionEntity> ocasiones) {
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

    public PrendaEntity(String name, MarcaEntity brandEntity, String type, String imageUrl, ColorEntity color, ClimaEntity climaEntity, Set<OcasionEntity> ocasionesEntities) {
        this.nombre = name;
        this.marca = brandEntity;
        this.tipo = type;
        this.imagenUrl = imageUrl;
        this.color = color;
        this.climaAdecuado = climaEntity;
        this.ocasiones = ocasionesEntities;
    }

    public PrendaEntity(String name, MarcaEntity brandEntity, String type, String imageUrl, String garmentCode, ColorEntity colorEntity, ClimaEntity climaEntity, Set<OcasionEntity> ocasionesEntities, String genero) {
        this.nombre = name;
        this.marca = brandEntity;
        this.tipo = type;
        this.imagenUrl = imageUrl;
        this.garmentCode = garmentCode;
        this.color = colorEntity;
        this.climaAdecuado = climaEntity;
        this.ocasiones = ocasionesEntities;
        this.genero = genero;
    }

    public static PrendaModel convertToModel(PrendaEntity prendaEntity) {
        BrandModel marcaModel = MarcaEntity.convertToModelWithoutPrendas(prendaEntity.getMarca());

        ClimaModel climaModel = null;
        if (prendaEntity.getClimaAdecuado() != null) {
            climaModel = new ClimaModel(
                    prendaEntity.getClimaAdecuado().getId(),
                    prendaEntity.getClimaAdecuado().getNombre()
            );
        } else {
            System.err.println("Prenda sin clima: " + prendaEntity.getId() + " - " + prendaEntity.getNombre());
        }

        Set<OcasionModel> ocasionesModels = prendaEntity.getOcasiones() != null
                ? prendaEntity.getOcasiones().stream()
                .map(ocasionEntity -> new OcasionModel(ocasionEntity.getId(), ocasionEntity.getNombre()))
                .collect(Collectors.toSet())
                : Set.of();

        ColorModel colorModel = prendaEntity.getColor() != null
                ? new ColorModel(prendaEntity.getColor().getId(), prendaEntity.getColor().getNombre(), prendaEntity.getColor().getValor())
                : null;

        PrendaModel model = new PrendaModel(
                prendaEntity.getNombre(),
                marcaModel,
                prendaEntity.getTipo(),
                prendaEntity.getImagenUrl(),
                prendaEntity.getGarmentCode(),
                colorModel,
                climaModel,
                ocasionesModels,
                prendaEntity.getGenero()
        );

        model.setId(prendaEntity.getId());
        return model;
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