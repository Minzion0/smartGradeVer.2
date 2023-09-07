package com.green.smartgradever2.admin.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AdminLectureDetailVo {
    private String lectureName;
    private String buildingName;
    private String lectureRoomName;
    private int currentPeople;
    private int score;
    private int dayWeek;
    private String year;
    private LocalDate lectureStrDate;
    private LocalDate lectureEndDate;
    private String lectureStrTime;
    private String lectureEndTime;
    private int gradeLimit;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private String textBook;
    private String ctnt;
    private String bookUrl;
}
