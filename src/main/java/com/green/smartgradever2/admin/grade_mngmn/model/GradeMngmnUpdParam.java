package com.green.smartgradever2.admin.grade_mngmn.model;

import lombok.Data;

@Data
public class GradeMngmnUpdParam {
    private Integer studentNum;
    private int grade;
    private int semester;
    private int avgScore;
    private double avgRating;
}
