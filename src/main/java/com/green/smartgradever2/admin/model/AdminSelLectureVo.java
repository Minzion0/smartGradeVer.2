package com.green.smartgradever2.admin.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AdminSelLectureVo {
    private Long ilecture;
    private String lectureNm;
    private int semester;
    private String majorName;
    private String nm;
    private String lectureRoomNm;
    private String buildingNm;
    private int gradeLimit;
    private int score;
    private LocalDate strDate;
    private LocalDate endDate;
    private LocalTime strTime;
    private LocalTime  endTime;
    private int maxPeople;
    private int dayWeek;
    private Long currentPeople;
    private int procedures;
    private int delYn;
}
