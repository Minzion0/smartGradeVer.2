package com.green.smartgradever2.professor.professorgrade.model;

import lombok.Data;
import org.springframework.security.core.parameters.P;

@Data
public class ProfessorGradeMngmnSelVo {
    private Long ilectureStudent;
    private Long ilecture;
    private Long studentNum;
    private String majorName;
    private String gender;
    private String grade;
    private String phone;
    private int year;
    private String nm;


}
