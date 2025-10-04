package com.outfitlab.project.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GarmentUploadResponse {
    private List<String> imageUrls;
    private String message;
}