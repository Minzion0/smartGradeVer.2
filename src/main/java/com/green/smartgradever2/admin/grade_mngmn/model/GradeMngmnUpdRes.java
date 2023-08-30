package com.green.smartgradever2.admin.grade_mngmn.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GradeMngmnUpdRes {
    private Integer studentNum;
    private int grade;
    private Long isemester;
    private int avgScore;
    private double avgRating;
}
