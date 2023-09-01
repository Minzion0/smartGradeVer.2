package com.green.smartgradever2.lecture_apply.model;

import lombok.Data;

import java.time.LocalTime;
@Data
public class LectureApplySelDto {
    private Long ilecture;
    private int openingProceudres;
    private Long ilectureName;
    private String lectureName;
    private int score;
    private Long ilectureRoom;
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
    private String booUrl;
    private int delYn;
}
