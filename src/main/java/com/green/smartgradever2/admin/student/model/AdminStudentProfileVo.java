package com.green.smartgradever2.admin.student.model;

import com.green.smartgradever2.config.entity.model.GenderEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminStudentProfileVo {

    private int StudentNum;
    private Long imajor;
    private int grade;
    private String  name;
    private GenderEnum gender;
    private String pic;
    private LocalDate birthdate;
    private String phone;
    private String email;
    private String address;
    private int finishedYn;
    private int score;
}
