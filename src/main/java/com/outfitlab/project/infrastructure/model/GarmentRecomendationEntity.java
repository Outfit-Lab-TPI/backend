package com.outfitlab.project.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.outfitlab.project.domain.model.GarmentRecomendationModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "garment_recomendation")
public class GarmentRecomendationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topGarment_id", nullable = false)
    @JsonIgnoreProperties({"marca", "hibernateLazyInitializer", "handler"})
    private PrendaEntity topGarment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bottomGarment_id", nullable = false)
    @JsonIgnoreProperties({"marca", "hibernateLazyInitializer", "handler"})
    private PrendaEntity bottomGarment;

    public GarmentRecomendationEntity() {}

    public GarmentRecomendationEntity(PrendaEntity topGarment, PrendaEntity bottomGarment) {
        this.topGarment = topGarment;
        this.bottomGarment = bottomGarment;
    }


    public static GarmentRecomendationModel convertToModel(GarmentRecomendationEntity entity) {
        return new GarmentRecomendationModel(
                PrendaEntity.convertToModel(entity.getTopGarment()),
                PrendaEntity.convertToModel(entity.getBottomGarment())
        );
    }

    public static GarmentRecomendationEntity convertToEntity(GarmentRecomendationModel model) {
        return new GarmentRecomendationEntity(
                PrendaEntity.convertToEntity(model.getTopGarment()),
                PrendaEntity.convertToEntity(model.getBottomGarment())
        );
    }
}
