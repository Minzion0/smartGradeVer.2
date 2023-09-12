package com.green.smartgradever2.student.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class StudentProfileLectureVo {
    private Long ilecture;
    private String lectureName;
    private LocalDate  lectureStrDate;
    private LocalDate  lectureEndDate;
    private LocalTime lectureStrTime;
    private LocalTime  lectureEndTime;
    private int score;
    private int dayWeek;


}
