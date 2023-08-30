package com.green.smartgradever2.professor.professorgrade.model;

import lombok.Data;

@Data
public class ProfessorGradeMngmnUpDto {
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int totalScore;
    private int finishedYn;
    private Long ilectureStudent;
    private Long ipofessor;
    private double point;
    private String rating;
    private Long ilecture;
}
