package com.green.smartgradever2.admin.student.model;

import com.green.smartgradever2.config.entity.model.GenderEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminInsStudentVo {


    private Long  studentNum;
    private Long imajor;
    private String nm;
    private GenderEnum gender;
    private LocalDate birthdate;
    private String phone;
    private int finishedYn;
    private String  createdAt;
    private int grade;
    private int delYn;


}
