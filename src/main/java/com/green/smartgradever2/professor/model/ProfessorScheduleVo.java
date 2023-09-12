package com.green.smartgradever2.professor.model;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ProfessorScheduleVo  {
    private LocalTime startTime;
    private LocalTime  endTime;
    private int dayWeek;
    private String lectureName;
    private String lectureRoomName;
    private String buildingName;
}
