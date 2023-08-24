package com.green.smartgradever2.professor.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfessorLectureDto {
    private Long ilecture;
    private Long ilectureName;
    private Long ilectureRoom;
    private Long isemester;
    private int openingProceudres;
    private int gradeLimit;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int lectureMaxPeople;
    private LocalDate lectureEndDate;
    private LocalDate studentsApplyDeadline;
    private String ctnt;
    private String textbook;
    private int delYn;
}
