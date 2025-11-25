package com.outfitlab.project.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="prenda_ocasion")
public class PrendaOcasionEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prenda_id", nullable = false)
    private PrendaEntity prenda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ocasion_id", nullable = false)
    private OcasionEntity ocasion;
}
