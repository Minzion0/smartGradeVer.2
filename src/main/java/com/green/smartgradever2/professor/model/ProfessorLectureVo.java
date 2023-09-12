package com.green.smartgradever2.professor.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ProfessorLectureVo {
    private Long ilecture;
    private LocalDate lectureStrDate;
    private LocalDate lectureEndDate;
    private LocalTime lectureStrTime;
    private LocalTime  lectureEndTime;
    private String lectureName;
    private int dayWeek;
}
