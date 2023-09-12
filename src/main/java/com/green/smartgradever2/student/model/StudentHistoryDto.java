package com.green.smartgradever2.student.model;

import lombok.Data;

import java.time.LocalTime;

@Data
public class StudentHistoryDto {
    private int year;
    private Long isemester;
    private String lectureName;
    private String professorName;
    private int score;
    private LocalTime lectureStrTime;
    private LocalTime lectureEndTime;
    private int     grade;
    private int finishedYn;
    private int dayWeek;
    private String textbook;
    private String bookUrl;
    private String ctnt;
}
