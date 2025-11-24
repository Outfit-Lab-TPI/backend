package com.outfitlab.project.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class BrandModel {

    private String codigoMarca;
    private String nombre;
    private String urlSite;
    private String logoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PrendaModel> prendas;

    public BrandModel(){}

    public BrandModel(String codigoMarca, String nombre, String logoUrl, LocalDateTime createdAt, LocalDateTime updatedAt, List<PrendaModel> prendas) {
        this.codigoMarca = codigoMarca;
        this.nombre = nombre;
        this.logoUrl = logoUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.prendas = prendas;
    }

    public BrandModel(String codigoMarca, String nombre, String logoUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.codigoMarca = codigoMarca;
        this.nombre = nombre;
        this.logoUrl = logoUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BrandModel(String codigoMarca, String nombre, String logoUrl, String urlSite) {
        this.codigoMarca = codigoMarca;
        this.nombre = nombre;
        this.urlSite = urlSite;
        this.logoUrl = logoUrl;
    }

    public BrandModel(String codigoMarca, String nombre, String logoUrl) {
        this.codigoMarca = codigoMarca;
        this.nombre = nombre;
        this.logoUrl = logoUrl;
    }

    public String getCodigoMarca() {
        return codigoMarca;
    }

    public void setCodigoMarca(String codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<PrendaModel> getPrendas() {
        return prendas;
    }

    public void setPrendas(List<PrendaModel> prendas) {
        this.prendas = prendas;
    }

    public String getUrlSite() {
        return urlSite;
    }

    public void setUrlSite(String urlSite) {
        this.urlSite = urlSite;
    }
}
