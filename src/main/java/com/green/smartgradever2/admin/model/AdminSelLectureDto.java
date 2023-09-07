package com.green.smartgradever2.admin.model;

import lombok.Data;

@Data
public class AdminSelLectureDto {
    private Integer procedures;
    private Long ilectureName;
    private String nm;
    private int strIdx;
    private int row;

    public AdminSelLectureDto() {
    }

    public AdminSelLectureDto(AdminSelLectureParam param) {
        this.ilectureName=param.getIlectureName();
        this.procedures = param.getProcedures();
        this.nm = param.getNm();

    }
}
