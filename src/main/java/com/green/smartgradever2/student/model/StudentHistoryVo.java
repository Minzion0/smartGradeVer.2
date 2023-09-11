package com.green.smartgradever2.student.model;

import lombok.Builder;
import lombok.Data;
import org.apache.poi.openxml4j.opc.PackageRelationship;

import java.time.LocalTime;

@Data
@Builder
public class StudentHistoryVo {
    private int year;
    private int grade;
    private int semester;
    private String lectureName;
    private String professorName;
    private int score;
    private int lectureScore;

}
