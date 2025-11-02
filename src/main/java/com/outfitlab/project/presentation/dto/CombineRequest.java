package com.outfitlab.project.presentation.dto;

import com.outfitlab.project.domain.model.dto.CombineRequestModel;

public class CombineRequest {
    private String top;
    private String bottom;
    private Boolean isMan;
    private String avatarType;

    public String getAvatarType() {return avatarType;}
    public void setAvatarType(String avatarType) {this.avatarType = avatarType;}
    public String getTop() { return top; }
    public void setTop(String top) { this.top = top; }
    public String getBottom() { return bottom; }
    public void setBottom(String bottom) { this.bottom = bottom; }
    public Boolean getIsMan() {return isMan;}
    public void setIsMan(Boolean isMan) {this.isMan = isMan;}

    public static CombineRequestModel convertToDomainModel(CombineRequest request){
        return new CombineRequestModel(
                request.getTop(),
                request.getBottom(),
                request.getIsMan(),
                request.getAvatarType());
    }
}
