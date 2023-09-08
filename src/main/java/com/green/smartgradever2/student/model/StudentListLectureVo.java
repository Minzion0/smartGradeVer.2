package com.green.smartgradever2.student.model;

import lombok.Data;

import java.time.LocalTime;

@Data
public class StudentListLectureVo {
    private Long ilecture;
    private int openingProcedures;
    private Long ilectureName;
    private String lectureName;
    private int score;
    private Long ilectureRoom;
    private String buildingName;
    private String lectureRoomName;
    private Long isemester;
    private LocalTime lectureStrTime;
    private LocalTime lectureEndTime;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int lectureMaxPeople;
    private int gradeLimit;
    private int dayWeek;
    private String ctnt;
    private String bookUrl;
    private String textBook;
    private int delYn;
    private String professorName;
}