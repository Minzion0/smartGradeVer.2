package com.green.smartgradever2.student.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data

public class StudentProfileDto {
    private Long studentNum;
    private Long imajor;

    private String nm;
    private int grade;
    private Enum gender;
    private String address;
    private String phone;
    private LocalDate birthDate;
    private String email;
    private String pic;
    private int finishedYn;
    private String role;
    private List<StudentLectureDto> attendedLectures;
}
