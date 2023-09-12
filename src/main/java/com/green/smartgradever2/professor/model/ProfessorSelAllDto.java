package com.green.smartgradever2.professor.model;

import lombok.Data;

@Data
public class ProfessorSelAllDto {
    private Long ilecture;
    private int year;
    private String lectureName;
    private String buildingName;
    private String lectureRoomName;
    private Long isemester;
    private String lectureStrTime;
    private String lectureEndTime;
    private int openingProceudres;
    private int gradeLimit;
    private int lectureMaxPeople;
    private int score;
    private int delYn;
    private int dayWeek;
    private int studentCount;
    private String bookUrl;
    private String ctnt;
    private String textbook;
}
