package com.green.smartgradever2.student.model;

import lombok.Data;

@Data
public class StudentListLectureDto {
    private int openingProcedures;
    private int gradeLimit;
    private int page;
    private Long studentNum;
    private String lectureName;
    private Long ilecture;
}
