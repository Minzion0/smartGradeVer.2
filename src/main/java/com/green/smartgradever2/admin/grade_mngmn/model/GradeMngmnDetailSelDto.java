package com.green.smartgradever2.admin.grade_mngmn.model;

import lombok.Data;

@Data
public class GradeMngmnDetailSelDto {
    private int totalScore;
    private int score;
    private Long studentNum;
    private String name;
    private String rating;
}
