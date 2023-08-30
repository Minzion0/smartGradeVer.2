package com.green.smartgradever2.admin.grade_mngmn.model;

import lombok.Data;

@Data
public class GradeMngmnUpdDto {
    private Integer studentNum;
    private int grade;
    private Long isemester;
    private int avgScore;
    private double avgRating;
}
