package com.outfitlab.project.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.outfitlab.project.domain.model.MarcaModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    public static MarcaModel convertToModel(MarcaEntity marcaEntity) {
        return new MarcaModel(
                marcaEntity.getCodigoMarca(),
                marcaEntity.getNombre(),
                marcaEntity.getLogoUrl(),
                marcaEntity.getCreatedAt(),
                marcaEntity.getUpdatedAt()
        );
    }

    public static MarcaEntity convertToEntity(MarcaModel marcaModel) {
        return new MarcaEntity(
                marcaModel.getCodigoMarca(),
                marcaModel.getNombre(),
                marcaModel.getLogoUrl(),
                marcaModel.getCreatedAt(),
                marcaModel.getUpdatedAt()
        );
    }
    //--------------------------------------------------------
}
