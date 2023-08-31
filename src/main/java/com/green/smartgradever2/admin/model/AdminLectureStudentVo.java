package com.green.smartgradever2.admin.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminLectureStudentVo {
    private Long istudent;
    private String nm;
    private String majorNm;
    private int attendance;
    private int minEx;
    private int finEx;
    private int totalScore;
    private double avg;
    private String gread;
}
