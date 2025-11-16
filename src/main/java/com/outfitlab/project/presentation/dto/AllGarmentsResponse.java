package com.outfitlab.project.presentation.dto;

import java.util.Map;

public class AllGarmentsResponse {
    Map<String, Object> topGarments;
    Map<String, Object> bottomsGarments;

    public AllGarmentsResponse() {}

    public AllGarmentsResponse(Map<String, Object> topGarments, Map<String, Object> bottomsGarments) {
        this.topGarments = topGarments;
        this.bottomsGarments = bottomsGarments;
    }

    public Map<String, Object> getTopGarments() {
        return topGarments;
    }

    public void setTopGarments(Map<String, Object> topGarments) {
        this.topGarments = topGarments;
    }

    public Map<String, Object> getBottomsGarments() {
        return bottomsGarments;
    }

    public void setBottomsGarments(Map<String, Object> bottomsGarments) {
        this.bottomsGarments = bottomsGarments;
    }
}
