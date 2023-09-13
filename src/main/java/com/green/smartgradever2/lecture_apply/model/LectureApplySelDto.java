package com.green.smartgradever2.lecture_apply.model;

import lombok.Data;

import java.time.LocalTime;
@Data
public class LectureApplySelDto {
    private Long ilecture;
    private Integer openingProcedures;
    private Long ilectureName;
    private String lectureName;
    private int score;
    private Long ilectureRoom;
    private String buildingName;
    private String lectureRoomName;
    private Long isemester;
    private String lectureStrTime;
    private String lectureEndTime;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int lectureMaxPeople;
    private int gradeLimit;
    private int dayWeek;
    private String ctnt;
    private String bookUrl;
    private String textbook;
    private int delYn;
    private Long studentCount;
}
