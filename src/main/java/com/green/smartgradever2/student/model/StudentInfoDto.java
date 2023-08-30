package com.green.smartgradever2.student.model;

import lombok.Data;

@Data
public class StudentInfoDto {
    private Integer studentNum;
    private String majorName;
    private int selfStudyCredit;
    private int remainingPoints;
    private int graduationScore;
}
