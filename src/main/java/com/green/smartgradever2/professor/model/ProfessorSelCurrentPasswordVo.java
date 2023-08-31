package com.green.smartgradever2.professor.model;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfessorSelCurrentPasswordVo {
    private Long id;
    private String role;
    private String currentStudentPassword;
}
