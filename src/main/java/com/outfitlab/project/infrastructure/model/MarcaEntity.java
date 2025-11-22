package com.outfitlab.project.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.PrendaModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "marca")
public class MarcaEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String codigoMarca;
    @Column(nullable = false)
    private String logoUrl;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PrendaEntity> prendas = new ArrayList<>();

    public MarcaEntity(){}

    public MarcaEntity(String codigoMarca, String nombre, String logoUrl) {
        this.codigoMarca = codigoMarca;
        this.nombre = nombre;
        this.logoUrl = logoUrl;
    }

    public MarcaEntity(String codigoMarca, String nombre, String logoUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.codigoMarca = codigoMarca;
        this.nombre = nombre;
        this.logoUrl = logoUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addPrenda(PrendaEntity prenda) {
        this.prendas.add(prenda);
        prenda.setMarca(this);
    }


    // ------------- acá hacemos los dos convert ------------
    public static MarcaEntity convertToEntityWithoutPrendas(BrandModel entity) {
        return new MarcaEntity(
                entity.getCodigoMarca(),
                entity.getNombre(),
                entity.getLogoUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static BrandModel convertToModelWithoutPrendas(MarcaEntity model) {
        return new BrandModel(
                model.getCodigoMarca(),
                model.getNombre(),
                model.getLogoUrl(),
                model.getCreatedAt(),
                model.getUpdatedAt()
        );
    }

    public static BrandModel convertToModel(MarcaEntity entity) {
        if (entity == null) return null;

        List<PrendaModel> prendasModel = new ArrayList<>();
        if (entity.getPrendas() != null) {
            for (PrendaEntity prendaEntity : entity.getPrendas()) {
                prendasModel.add(PrendaEntity.convertToModel(prendaEntity));
            }
        }

        return new BrandModel(
                entity.getCodigoMarca(),
                entity.getNombre(),
                entity.getLogoUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                prendasModel
        );
    }

    public static MarcaEntity convertToEntity(BrandModel model) {
        if (model == null) return null;

        MarcaEntity entity = new MarcaEntity();
        entity.setCodigoMarca(model.getCodigoMarca());
        entity.setNombre(model.getNombre());
        entity.setLogoUrl(model.getLogoUrl());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());

        List<PrendaEntity> prendasEntity = new ArrayList<>();
        if (model.getPrendas() != null) {
            for (PrendaModel prendaModel : model.getPrendas()) {
                PrendaEntity prendaEntity = PrendaEntity.convertToEntity(prendaModel);
                prendaEntity.setMarca(entity);
                prendasEntity.add(prendaEntity);
            }
        }

        entity.setPrendas(prendasEntity);
        return entity;
    }
    //--------------------------------------------------------
}
