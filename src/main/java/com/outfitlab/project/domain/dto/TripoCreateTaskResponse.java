package com.outfitlab.project.domain.dto;

import lombok.Data;

@Data
public class TripoCreateTaskResponse {
    private int code;
    private TaskData data;
    
    @Data
    public static class TaskData {
        private String task_id;
    }
}