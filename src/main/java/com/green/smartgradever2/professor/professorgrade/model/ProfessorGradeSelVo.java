package com.green.smartgradever2.professor.professorgrade.model;

import lombok.Data;

@Data
public class ProfessorGradeSelVo {
    private String studentNum;
    private String nm;
    private String majorName;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private double point; // 4.5
    private String grade;
}
