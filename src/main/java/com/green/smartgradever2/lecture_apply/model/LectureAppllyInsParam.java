package com.green.smartgradever2.lecture_apply.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LectureAppllyInsParam {
    private Long ilectureName;
    private Long ilectureRoom;
    private String lectureStrTime;
    private String lectureEndTime;
    private String dayWeek;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int lectureMaxPeople;
    private int gradeLimit;
    private String ctnt;
    private String textBook;
    private String bookUrl;
}
