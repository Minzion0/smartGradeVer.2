package com.green.smartgradever2.student.model;

import lombok.Data;

import java.time.LocalTime;

@Data
public class StudentScheduleVo {
    private LocalTime  startTime;
    private LocalTime  endTime;
    private int dayWeek;
    private String lectureName;
    private String lectureRoomName;
    private String buildingName;
}
