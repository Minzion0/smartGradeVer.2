package com.green.smartgradever2.admin.student.model;

import com.green.smartgradever2.config.entity.model.GenderEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AdminStudentFindVo {
    private int studentNum;
    private int grade;
    private String nm;
    private String majorName;
    private GenderEnum gender;
    private LocalDate birthdate;
    private String phone;
    private LocalDate createdAt;
    private int finishedYn;
    private int score;
}
