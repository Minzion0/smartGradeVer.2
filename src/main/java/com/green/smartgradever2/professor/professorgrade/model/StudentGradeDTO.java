package com.green.smartgradever2.professor.professorgrade.model;

import lombok.Data;

@Data
public class StudentGradeDTO {
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int totalScore;
    private String grade;
    private String rating;
}
