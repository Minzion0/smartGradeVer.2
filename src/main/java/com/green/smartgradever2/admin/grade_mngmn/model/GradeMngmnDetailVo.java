package com.green.smartgradever2.admin.grade_mngmn.model;

import com.green.smartgradever2.config.entity.model.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
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
}
