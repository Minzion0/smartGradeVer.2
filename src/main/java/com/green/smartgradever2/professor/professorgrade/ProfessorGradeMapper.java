package com.green.smartgradever2.professor.professorgrade;


import com.green.smartgradever2.professor.professorgrade.model.ProfessorGradeMngmnSelDto;
import com.green.smartgradever2.professor.professorgrade.model.ProfessorGradeMngmnSelVo;
import com.green.smartgradever2.professor.professorgrade.model.ProfessorGradeMngmnUpDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ProfessorGradeMapper {
    int upMngnm(ProfessorGradeMngmnUpDto dto);
    List<ProfessorGradeMngmnSelVo> selStudentScore (ProfessorGradeMngmnSelDto dto);
    int selStudentScoreCount(ProfessorGradeMngmnSelDto dto);


    int getMaxAttendance(Long ilecture);
    int getMaxMidtermExamination(Long ilecture);
    int getMaxFinalExamination(Long ilecture);


}
