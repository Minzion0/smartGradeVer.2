package com.green.smartgradever2.student.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class StudentLectureDto {
    private Long ilectureStudent;
    private Long ilecture;
    private int finishedYn;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int totalScore;
    private LocalDate finishedAt;
    private LocalDate correctionAt;
    private int dayWeek;
    private LocalTime lectureStrTime;
    private LocalTime lectureEndTime;
}
