package com.green.smartgradever2.admin.grade_mngmn.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GradeMngmnStudentVo {
    private Long studentNum;
    private String name;

    public GradeMngmnStudentVo(Long studentNum, String name) {
        this.studentNum = studentNum;
        this.name = name;
    }
}
