package com.green.smartgradever2.student.model;

import lombok.Data;

import java.time.LocalDate;
@Data
public class StudentProfileVo {
    private Long studentNum;
    private String majorName;
    private String name;
    private int grade;
    private Enum gender;
    private String address;
    private String phone;
    private LocalDate birthdate;
    private String email;
    private String pic;
    private int finishedYn;
    private int score;
    private String secretKey;

}
