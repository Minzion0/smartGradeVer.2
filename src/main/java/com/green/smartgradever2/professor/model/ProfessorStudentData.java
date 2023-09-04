package com.green.smartgradever2.professor.model;

import lombok.Data;

@Data
public class ProfessorStudentData {
    private Long StudentNum;
    private String StudentName;
    private String grade;
    private int totalScore;
}
