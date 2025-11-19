package com.outfitlab.project.domain.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El formato del correo electrónico es inválido.")
    private String email;

    @NotBlank(message = "El nombre es obligatorio.")
    private String name;

    @NotBlank(message = "El apellido es obligatorio.")
    private String lastName;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres de longitud.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "La contraseña debe contener al menos una mayúscula y un número."
    )
    private String password;

    public RegisterDTO() {
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}