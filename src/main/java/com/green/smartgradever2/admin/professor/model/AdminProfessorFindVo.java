package com.green.smartgradever2.admin.professor.model;

import com.green.smartgradever2.entity.model.GenderEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminProfessorFindVo {
    private Long iprofessor;
    private String majorName;
    private String nm;
    private GenderEnum gender;
    private LocalDate birthdate;
    private String phone;
    private String email;
    private String address;
    private LocalDateTime createdAt;
    private int delYn;
}
