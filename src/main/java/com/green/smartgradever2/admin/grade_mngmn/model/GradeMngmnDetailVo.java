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
@AllArgsConstructor
public class GradeMngmnDetailVo {
    private String name;
    private GenderEnum gender;
    private String phone;
    private Long studentNum;
    private String majorName;
    private LocalDateTime createdAt;
    private Integer scoreStudent;
    private int graduationScore;


}
