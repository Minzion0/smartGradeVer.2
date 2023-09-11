package com.green.smartgradever2.student.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class StudentScheduleRes {
    private String  startTime;
    private String   endTime;
    private int dayWeek;
    private String lectureName;
    private String lectureRoomName;

}
