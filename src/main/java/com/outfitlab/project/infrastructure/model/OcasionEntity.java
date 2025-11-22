package com.outfitlab.project.infrastructure.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ocasion")
public class OcasionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public OcasionEntity() {}

    public OcasionEntity(String nombre) {
        this.nombre = nombre;
    }
}