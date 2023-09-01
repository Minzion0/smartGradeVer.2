package com.green.smartgradever2.student.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class StudentRegisterRes {
    private boolean success;
    private String message;
    private Long ilectureStudent;
    private Long ilecture;
    private Long studentNum;
    private String lectureName;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int totalScore;
    private LocalDate finishedAt;
    private LocalDate correctionAt;
    private int finishedYn;
    private int delYn;
    private int dayWeek;
    private LocalTime lectureStrTime;
    private LocalTime lectureEndTime;
    private int objection;
}
