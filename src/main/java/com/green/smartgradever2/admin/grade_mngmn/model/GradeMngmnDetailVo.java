package com.green.smartgradever2.admin.grade_mngmn.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeMngmnDetailVo {
    private String pic;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String phone;
    private String address;
    private int studentNum;
    private String majorName;
    private LocalDate createdAt;
    private String email;
    private int scoreStudent;
    private int graduationScore;
}
