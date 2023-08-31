package com.green.smartgradever2.professor.model;

import lombok.Data;

@Data
public class ProfessorUpdPasswordParam {
    private String currentProfessorPassword;
    private String professorPassword;
}