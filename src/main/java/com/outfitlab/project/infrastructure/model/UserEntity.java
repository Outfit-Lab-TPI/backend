package com.outfitlab.project.infrastructure.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {
    private int id;
    private String satulation;
    private String name;
    private String secondName;
    private String lastName;
    private double years;
    private String email;

    public UserEntity(String name, String lastName, String email, String satulation, String secondName, double years) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.satulation = satulation;
        this.secondName = secondName;
        this.years = years;
    }
}
