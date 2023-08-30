package com.green.smartgradever2.admin.student.model;

import com.green.smartgradever2.config.entity.model.GenderEnum;
import lombok.Data;

import java.time.LocalDate;
@Data
public class AdminInsStudentParam {

    private Long imajor;
    private String nm;
    private GenderEnum gender;
    private LocalDate birthdate;
    private String phone;

}
