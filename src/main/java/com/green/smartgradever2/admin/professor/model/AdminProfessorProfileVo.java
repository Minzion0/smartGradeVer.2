package com.green.smartgradever2.admin.professor.model;

import com.green.smartgradever2.config.entity.model.GenderEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdminProfessorProfileVo {
    private Long iprofessor;
    private String name;
    private GenderEnum gender;
    private LocalDate birthdate;
    private String phone;
    private String pic;
    private String address;
    private String email;
    private Long imajor;
    private LocalDateTime createdAt;
    private int delYn;
}
