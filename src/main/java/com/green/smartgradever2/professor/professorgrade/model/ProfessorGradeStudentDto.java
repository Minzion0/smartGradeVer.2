package com.green.smartgradever2.professor.professorgrade.model;

import com.green.smartgradever2.student.model.StudentLectureDto;
import lombok.Data;

import java.util.List;

@Data
public class ProfessorGradeStudentDto {
    private Long iprofessor;
    private List<ProStudentLectureDto> lectureList;
}
