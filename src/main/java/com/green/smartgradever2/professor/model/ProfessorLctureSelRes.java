package com.green.smartgradever2.professor.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class ProfessorLctureSelRes {
    private PagingUtils page;
    private List<ProfessorLectureDto> lectureList;
}
