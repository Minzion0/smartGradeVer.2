package com.green.smartgradever2.professor.professorgrade.model;

import lombok.Data;

@Data
public class ProfessorGradeMngmnSelDto {
    private Long iprofessor;
    private int page;
    private int ilecture;
    private int studentNum;
    private int staIdx;
    private String nm;
    private int year;
}
