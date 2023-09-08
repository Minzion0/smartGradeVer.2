package com.green.smartgradever2.professor.professorgrade.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ProStudentLectureDto {
    private Long ilectureStudent;
    private Long ilecture;
    private Long sudentNum;
    private String majorName;
    private String StudentName;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int dayWeek;
       private int totalScore;
    private String grade;
    private double rating;
}
