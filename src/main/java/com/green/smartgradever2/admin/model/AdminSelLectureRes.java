package com.green.smartgradever2.admin.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AdminSelLectureRes {
    private Long ilecture;
    private String lectureNm;
    private int semester;
    private String majorName;
    private String year;

    private String nm;
    private String lectureRoomNm;
    private String buildingNm;
    private int gradeLimit;
    private int score;
    private LocalDate strDate;
    private LocalDate endDate;
    private String  strTime;
    private String  endTime;
    private int maxPeople;
    private Long currentPeople;
    private int dayWeek;
    private int procedures;
    private int delYn;
}
