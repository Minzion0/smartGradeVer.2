package com.green.smartgradever2.lecture_apply.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LectureAppllyInsDto {

    private Long ilectureName;
    private Long ilectureRoom;
    private Long iprofessor;
    private Long isemester;
    private LocalDate lectureStrDate;
    private LocalDate lectureEndDate;
    private String lectureStrTime;
    private String lectureEndTime;
    private String dayWeek;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int lectureMaxPeople;
    private int garedLimit;
    private int delYn;
}
