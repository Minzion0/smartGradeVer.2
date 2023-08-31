package com.green.smartgradever2.student.model;

import lombok.Data;

@Data
public class StudentSelVo {
    private Long studentNum;
    private Long ilectureStudent;
    private Long ilecture;
    private int finishedYn;
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int totalScore;
    private String grade;
    private String rating;
}
