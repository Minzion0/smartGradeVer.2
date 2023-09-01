package com.green.smartgradever2.professor.model;

import lombok.Data;

@Data
public class ProfessorSelAllDto {
    private Long ilecture;
    private String lectureName;
    private String lectureRoomName;
    private Long isemester;
    private int openingProceudres;
    private int gradeLimit;
    private int lectureMaxPeople;
    private int score;
    private int delYn;
}
