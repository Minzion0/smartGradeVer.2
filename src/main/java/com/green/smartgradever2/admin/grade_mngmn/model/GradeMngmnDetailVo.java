package com.green.smartgradever2.admin.grade_mngmn.model;

import com.green.smartgradever2.config.entity.model.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class GradeMngmnDetailVo {
    private String pic;
    private String name;
    private GenderEnum gender;
    private LocalDate birthDate;
    private String phone;
    private String address;
    private Long studentNum;
    private String majorName;
    private LocalDateTime createdAt;
    private String email;
    private int scoreStudent;
    private int graduationScore;

    public GradeMngmnDetailVo(String pic, String name, GenderEnum gender,
                              LocalDate birthDate, String phone, String address, Long studentNum,
                              String majorName, LocalDateTime createdAt, String email,
                              int scoreStudent, int graduationScore) {
        this.pic = pic;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
        this.studentNum = studentNum;
        this.majorName = majorName;
        this.createdAt = createdAt;
        this.email = email;
        this.scoreStudent = scoreStudent;
        this.graduationScore = graduationScore;
    }
}
