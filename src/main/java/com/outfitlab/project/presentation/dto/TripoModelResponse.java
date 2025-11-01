package com.outfitlab.project.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripoModelResponse {
    private String taskId;
    private String status;
    private String originalFilename;
    private String fileExtension;
    private String minioImagePath;
    private String tripoModelUrl;
    private String imageUrl;
    private String message;
}
