package com.green.smartgradever2.admin.major.model;

import lombok.Data;

@Data
public class AdminMajorDto {
    private Long imajor;
    private String majorName;
    private int graduationScore;
    private int delYn;
    private String remarks;
}
