package com.outfitlab.project.domain.dto;

import lombok.Data;

@Data
public class TripoTaskStatusResponse {
    private int code;
    private TaskStatusData data;
    
    @Data
    public static class TaskStatusData {
        private String task_id;
        private String status;
        private TaskOutput output;
        
        @Data
        public static class TaskOutput {
            private String model;
            private String rendered_image;
            private String pbr_model;
            private String base_model;
        }
    }
}