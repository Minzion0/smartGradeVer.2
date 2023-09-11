package com.green.smartgradever2.professor.professorgrade.model;

import lombok.Data;

@Data
public class ProStudentLectureVo {
    private Long ilectureStudent;
    private Long ilecture;
    private Long studentNum;
    private String majorName;
    private String StudentName;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int dayWeek;
    private int totalScore;
    private String grade;
    private String rating;
    private int finishedYn;
}
