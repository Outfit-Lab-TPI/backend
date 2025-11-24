package com.outfitlab.project.infrastructure.model;

import com.outfitlab.project.domain.model.CombinationModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "combination")
public class CombinationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prenda_superior_id", nullable = false)
    private PrendaEntity prendaSuperior;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prenda_inferior_id", nullable = false)
    private PrendaEntity prendaInferior;

    @OneToMany(mappedBy = "combination", fetch = FetchType.LAZY)
    private List<CombinationAttemptEntity> attempts;

    public CombinationEntity() {}

    public CombinationEntity(PrendaEntity prendaSuperior, PrendaEntity prendaInferior) {
        this.prendaSuperior = prendaSuperior;
        this.prendaInferior = prendaInferior;
    }

    public CombinationAttemptEntity getLastAttempt() {
        if (attempts == null || attempts.isEmpty()) {
            return null;
        }

        return attempts.stream()
                .max(Comparator.comparing(CombinationAttemptEntity::getCreatedAt))
                .orElse(null);
    }

    public CombinationModel convertToModel() {
        CombinationModel model = new CombinationModel(
               PrendaEntity.convertToModel(this.prendaSuperior),
               PrendaEntity.convertToModel(this.prendaInferior)
        );
        model.setId(this.id);
        return model;
    }

    public CombinationEntity fromModel(CombinationModel model) {
        PrendaEntity superior = PrendaEntity.convertToEntity(model.getPrendaSuperior());
        PrendaEntity inferior = PrendaEntity.convertToEntity(model.getPrendaInferior());

        CombinationEntity entity = new CombinationEntity(superior, inferior);
        entity.setId(model.getId());
        return entity;
    }

}
