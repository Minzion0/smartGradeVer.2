package com.green.smartgradever2.professor.professorgrade.model;

import lombok.Data;

@Data
public class ProStudentListVo {
    private Long ilectureStudent;
    private Long ilecture;
    private Long studentNum;
    private String majorName;
    private Enum gender;
    private String phone;
    private String studentName;
    private int grade;
}
