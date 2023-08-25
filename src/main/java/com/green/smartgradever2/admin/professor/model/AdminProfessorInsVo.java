package com.green.smartgradever2.admin.professor.model;

import com.green.smartgradever2.entity.model.GenderEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdminProfessorInsVo {
    private Long iprofessor;
    private Long imajor;
    private String nm;
    private GenderEnum gender;
    private LocalDate birthdate;
    private String phone;
    private LocalDateTime createdAt;
    private int delYn;
}
