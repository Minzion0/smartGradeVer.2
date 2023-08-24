package com.green.smartgradever2.admin.student.model;

import lombok.Data;

@Data
public class AdminStudentFindDto {
    private Long imajor;
    private int grade;
    private Integer studentNum;
    private String nm;
    private int finishedYn;
    private int staIdx;
    private int row;

}
