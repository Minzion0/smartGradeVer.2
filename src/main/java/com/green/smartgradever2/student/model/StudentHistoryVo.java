package com.green.smartgradever2.student.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class StudentHistoryVo {
    private int year;
    private int semester;
    private String lectureName;
    private String professorName;
    private int score;

}
