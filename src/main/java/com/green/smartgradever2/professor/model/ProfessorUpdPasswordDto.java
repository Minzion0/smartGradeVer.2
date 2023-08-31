package com.green.smartgradever2.professor.model;

import lombok.Data;

@Data
public class ProfessorUpdPasswordDto {
    private Long iprofessor;
    private String role;
    private String professorPassword;
}