package com.green.smartgradever2.professor.professorgrade.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProfessorGradeStudentRes {
    private Long iprofessor;
    private PagingUtils page;
    private List<ProStudentLectureVo> lectureList;
}
