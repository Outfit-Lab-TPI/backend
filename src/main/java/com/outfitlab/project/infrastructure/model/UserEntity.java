package com.outfitlab.project.infrastructure.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {
    private int id;
    private String name;

    public UserEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
