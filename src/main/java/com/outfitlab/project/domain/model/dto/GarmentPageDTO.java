package com.outfitlab.project.domain.model.dto;

import java.util.List;

public class GarmentPageDTO {

    private List<GarmentDTO> content; // la lista transformada
    private int page;                 // número de página actual
    private int size;                 // tamaño de página
    private long totalElements;       // total de elementos
    private int totalPages;           // total de páginas
    private boolean last;             // si es la última página

    public GarmentPageDTO() {}

    public GarmentPageDTO(List<GarmentDTO> dtoList, int number, int size, long totalElements, int totalPages, boolean last) {
        this.content = dtoList;
        this.page = number;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public List<GarmentDTO> getContent() {
        return content;
    }

    public void setContent(List<GarmentDTO> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
