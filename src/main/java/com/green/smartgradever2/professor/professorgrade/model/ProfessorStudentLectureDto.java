package com.green.smartgradever2.professor.professorgrade.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ProfessorStudentLectureDto {
    private Long ilectureStudent;
    private Long ilecture;
    private Long sudentNum;
    private String majorName;
    private Enum gender;
    private String phone;
    private String StudentName;
    private LocalDate lectureEndDate;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int dayWeek;
    private LocalTime lectureStrTime;
    private LocalTime lectureEndTime;
    private int totalScore;
    private String grade;
    private double rating;
}
