package com.green.smartgradever2.professor.professorgrade.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProfessorListStudentRes {
    private PagingUtils page;
    private List<ProStudentListVo> lecturelist;
}
