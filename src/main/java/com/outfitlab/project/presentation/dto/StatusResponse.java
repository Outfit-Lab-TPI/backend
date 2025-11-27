package com.outfitlab.project.presentation.dto;

import lombok.AllArgsConstructor;

import java.util.List;
@AllArgsConstructor
public class StatusResponse {
    private String id;
    private String status;
    private List<String> output;
    private Object error;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getOutput() { return output; }
    public void setOutput(List<String> output) { this.output = output; }
    public Object getError() { return error; }
    public void setError(Object error) { this.error = error; }
}