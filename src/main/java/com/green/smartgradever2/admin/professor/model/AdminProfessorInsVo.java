package com.green.smartgradever2.admin.professor.model;

import com.green.smartgradever2.config.entity.model.GenderEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminProfessorInsVo {
    private Long iprofessor;
    private Long imajor;
    private String nm;
    private GenderEnum gender;
    private LocalDate birthdate;
    private String phone;
    private String createdAt;
    private int delYn;
}
