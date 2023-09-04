package com.green.smartgradever2.student.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class StudentSelVo {
    private Long studentNum;
    private Long ilectureStudent;
    private Long ilecture;
    private int StudentGrade;
    private Long isemester;
    private int year;
    private String professorName;
    private String lectureName;
    private int dayWeek;
    private LocalTime lectureStrTime;
    private LocalTime lectureEndTime;
    private int score;
    private int finishedYn;
    private int objection;



    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int totalScore;
    private String grade;
    private String rating;
}
