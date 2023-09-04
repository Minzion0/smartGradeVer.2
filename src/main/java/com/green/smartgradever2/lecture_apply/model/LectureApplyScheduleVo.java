package com.green.smartgradever2.lecture_apply.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LectureApplyScheduleVo {
    private String  startTime;
    private String  endTime;
    private int dayWeek;
}
