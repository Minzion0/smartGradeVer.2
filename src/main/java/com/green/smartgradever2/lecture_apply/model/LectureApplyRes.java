package com.green.smartgradever2.lecture_apply.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LectureApplyRes {
    private Long ilecture;
    private Long ilectureName;
    private String lectureName;
    private int score;
    private Long ilectureRoom;
    private Long iprofessor;
    private Long isemester;
    private String lectureStrTime;
    private String lectureEndTime;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int lectureMaxPeople;
    private int gradeLimit;
    private int dayWeek;
    private int delYn;


}
