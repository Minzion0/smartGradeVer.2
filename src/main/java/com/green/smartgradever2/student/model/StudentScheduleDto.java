package com.green.smartgradever2.student.model;

import lombok.Data;

import java.time.LocalTime;

@Data
public class StudentScheduleDto {
    private int dayWeek; // 수업 요일
    private LocalTime lectureStrTime; // 강의 시작 시간
    private LocalTime lectureEndTime;
}
