package com.outfitlab.project.domain.model.dto;

import java.util.List;

public class PrendaDashboardDTO {
    public static record DailyPrueba(int dia, int pruebas) {}

    public static record TopPrenda(
            Long id,
            String nombre,
            String color,
            String imagenUrl,
            int pruebas,
            List<DailyPrueba> daily
    ) {}    public static record DiaPrueba(int dia, int pruebas) {}

    public static record ColorConversion(String color, int pruebas, int favoritos, double conversion) {}

    public static class ComboPopular {
        private final String superior;
        private final String inferior;
        private final String imgSup;
        private final String imgInf;
        private int pruebas;
        private int thumbs;

        public ComboPopular(String superior, String inferior, String imgSup, String imgInf, int pruebas, int thumbs) {
            this.superior = superior;
            this.inferior = inferior;
            this.imgSup = imgSup;
            this.imgInf = imgInf;
            this.pruebas = pruebas;
            this.thumbs = thumbs;
        }

        public String getSuperior() { return superior; }
        public String getInferior() { return inferior; }
        public String getImgSup() { return imgSup; }
        public String getImgInf() { return imgInf; }

        public int getPruebas() { return pruebas; }
        public void setPruebas(int pruebas) { this.pruebas = pruebas; }

        public int getThumbs() { return thumbs; }
        public void setThumbs(int thumbs) { this.thumbs = thumbs; }
    }

}