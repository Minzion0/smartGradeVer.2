package com.green.smartgradever2.student.model;

import lombok.Data;

@Data
public class StudentHistoryOpenDto {
    private Long studentNum;
    private Integer openingProcedures;
    private int year;
    private String lectureName;
    private String nm;
}
