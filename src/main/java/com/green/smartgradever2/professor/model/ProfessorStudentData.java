package com.green.smartgradever2.professor.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfessorStudentData {
    private Long StudentNum;
    private String StudentName;
    private String grade;
    private int totalScore;
    private String majorName;
    private Long ilectureStudent;
    private LocalDate correctionAt;
    private String lectureName;
}
