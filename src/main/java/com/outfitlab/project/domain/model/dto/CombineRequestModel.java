package com.outfitlab.project.domain.model.dto;

public class CombineRequestModel {
    private String top;
    private String bottom;
    private Boolean isMan;
    private String avatarType;

    public CombineRequestModel() {}
    public CombineRequestModel(String top, String bottom, Boolean isMan, String avatarType) {
        this.top = top;
        this.bottom = bottom;
        this.isMan = isMan;
        this.avatarType = avatarType;
    }

    public String getAvatarType() {return avatarType;}
    public void setAvatarType(String avatarType) {this.avatarType = avatarType;}
    public String getTop() { return top; }
    public void setTop(String top) { this.top = top; }
    public String getBottom() { return bottom; }
    public void setBottom(String bottom) { this.bottom = bottom; }
    public Boolean getIsMan() {return isMan;}
    public void setIsMan(Boolean isMan) {this.isMan = isMan;}

    @Override
    public String toString() {
        return "CombineRequestModel{" +
                "top='" + top + '\'' +
                ", bottom='" + bottom + '\'' +
                ", isMan=" + isMan +
                ", avatarType='" + avatarType + '\'' +
                '}';
    }
}
